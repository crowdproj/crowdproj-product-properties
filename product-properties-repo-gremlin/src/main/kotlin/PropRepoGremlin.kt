package com.crowdproj.marketplace.repository.gremlin

import com.benasher44.uuid.uuid4
import com.crowdproj.marketplace.common.helpers.asPropError
import com.crowdproj.marketplace.common.helpers.errorAdministration
import com.crowdproj.marketplace.common.helpers.errorRepoConcurrency
import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.repo.*
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_DESCRIPTION
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_LOCK
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.FIELD_NAME
import com.crowdproj.marketplace.repository.gremlin.PropGremlinConst.RESULT_LOCK_FAILURE
import com.crowdproj.marketplace.repository.gremlin.exceptions.DbDuplicatedElementsException
import com.crowdproj.marketplace.repository.gremlin.mappers.addProductProperty
import com.crowdproj.marketplace.repository.gremlin.mappers.label
import com.crowdproj.marketplace.repository.gremlin.mappers.listProductProperty
import com.crowdproj.marketplace.repository.gremlin.mappers.toProductProperty
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr

class PropRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    initObjects: List<ProductProperty> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },
) : IPropRepository {

    val initializedObjects: List<ProductProperty>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(productProperty: ProductProperty): ProductProperty = g.addV(productProperty.label())
        .addProductProperty(productProperty)
        .listProductProperty()
        .next()
        ?.toProductProperty()
        ?: throw RuntimeException("Cannot initialize object $productProperty")

    override suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        val key = randomUuid()
        val ad = rq.productProperty.copy(id = ProductPropertyId(key), lock = ProductPropertyLock(randomUuid()))
        val dbRes = try {
            g.addV(ad.label())
                .addProductProperty(ad)
                .listProductProperty()
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return ProductPropertyResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asPropError())
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> ProductPropertyResponse(
                data = dbRes.first().toProductProperty(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
        val keys = rq.ids.filter { it != ProductPropertyId.NONE }.map { it.asString() }
        if (keys.isEmpty()) return resultErrorEmptyIds
        val dbRes = try {
            g.V(keys).listProductProperty().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultsErrorNotFound(keys)
            }
            return ProductPropertiesResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asPropError())
            )
        }
        return ProductPropertiesResponse(
            data = dbRes.map { it.toProductProperty() },
            isSuccess = true
        )
    }

    override suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        val key = rq.productProperty.id.takeIf { it != ProductPropertyId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.productProperty.lock.takeIf { it != ProductPropertyLock.NONE } ?: return resultErrorEmptyLock
        val newLock = ProductPropertyLock(randomUuid())
        val newProp = rq.productProperty.copy(lock = newLock)

        return updateWithLock(key, oldLock, newProp, newLock)
    }

    override suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
        val key = rq.id.takeIf { it != ProductPropertyId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldProperty = runCatching { g.V(key).listProductProperty().next().toProductProperty() }
            .getOrElse {
                if (it is ResponseException || it.cause is ResponseException) {
                    return resultErrorNotFound(key)
                }
                return ProductPropertyResponse(
                    data = null,
                    isSuccess = false,
                    errors = listOf(it.asPropError())
                )
            }

        val oldLock = rq.lock.takeIf { it != ProductPropertyLock.NONE } ?: return resultErrorEmptyLock
        val newLock = ProductPropertyLock(randomUuid())
        val newProp = oldProperty.copy(lock = newLock, deleted = true)

        return updateWithLock(key, oldLock, newProp, newLock)
    }

    override suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
        val result = try {
            g.V()
                .apply {
                    rq.nameFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_NAME, TextP.containing(it)) }
                }
                .apply {
                    rq.descriptionFilter.takeIf { it.isNotBlank() }
                        ?.also { has(FIELD_DESCRIPTION, TextP.containing(it)) }
                }
                .listProductProperty()
                .toList()
        } catch (e: Throwable) {
            return ProductPropertiesResponse(
                isSuccess = false,
                data = null,
                errors = listOf(e.asPropError())
            )
        }
        return ProductPropertiesResponse(
            data = result.map { it.toProductProperty() },
            isSuccess = true
        )
    }

    private fun updateWithLock(
        key: String,
        oldLock: ProductPropertyLock,
        newProp: ProductProperty,
        newLock: ProductPropertyLock
    ): ProductPropertyResponse {
        val dbRes = try {
            g
                .V(key)
                .`as`("tmp")
                .choose(
                    gr.select<Vertex, Any>("tmp")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("tmp").addProductProperty(newProp).listProductProperty(),
                    gr.select<Vertex, Vertex>("tmp").listProductProperty(result = RESULT_LOCK_FAILURE)
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return ProductPropertyResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asPropError())
            )
        }
        val adResult = dbRes.firstOrNull()?.toProductProperty()
        return when {
            adResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            adResult.lock != newLock -> ProductPropertyResponse(
                data = adResult,
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock,
                        actualLock = adResult.lock,
                    ),
                )
            )

            else -> ProductPropertyResponse(
                data = adResult,
                isSuccess = true,
            )
        }
    }

    companion object {
        val resultErrorEmptyId = ProductPropertyResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        val resultErrorEmptyIds = ProductPropertiesResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    field = "ids",
                    message = "Ids must not be null or blank"
                )
            )
        )

        val resultErrorEmptyLock = ProductPropertyResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    field = "lock",
                    message = "Lock must be provided"
                )
            )
        )

        fun resultErrorNotFound(key: String, e: Throwable? = null) = ProductPropertyResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                PropError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found object with key $key",
                    exception = e
                )
            )
        )

        fun resultsErrorNotFound(keys: List<String>, e: Throwable? = null) = ProductPropertiesResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                PropError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found object with key $keys",
                    exception = e
                )
            )
        )

        fun errorDuplication(key: String) = ProductPropertyResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                errorAdministration(
                    violationCode = "duplicateObjects",
                    description = "Database consistency failure",
                    exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                )
            )
        )
    }
}