package com.crowdproj.marketplace.repository.inmemory

import com.benasher44.uuid.uuid4
import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.repo.*
import com.crowdproj.marketplace.repository.inmemory.model.ProductPropertyEntity
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PropRepoInMemory(
    initObjects: List<ProductProperty> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IPropRepository {
    private val cache = Cache.Builder<String, ProductPropertyEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(productProperty: ProductProperty) {
        val entity = ProductPropertyEntity(productProperty)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        val key = randomUuid()
        val productProperty =
            rq.productProperty.copy(id = ProductPropertyId(key), lock = ProductPropertyLock(randomUuid()))
        val entity = ProductPropertyEntity(productProperty)
        cache.put(key, entity)
        return ProductPropertyResponse(
            data = productProperty,
            isSuccess = true,
        )
    }

    override suspend fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
        val keys = rq.ids.filter { it != ProductPropertyId.NONE }.map { it.asString() }

        if (keys.isEmpty()) return resultErrorEmptyIds

        val propsFound = keys.mapNotNull { cache.get(it) }
        if (propsFound.isEmpty()) return resultsErrorNotFound

        return ProductPropertiesResponse(
            data = propsFound.map { it.toInternal() },
            isSuccess = true
        )
    }

    private suspend fun doUpdate(
        id: ProductPropertyId,
        oldLock: ProductPropertyLock,
        okBlock: (key: String, oldProductProperty: ProductPropertyEntity) -> ProductPropertyResponse
    ): ProductPropertyResponse {
        val key = id.takeIf { it != ProductPropertyId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockStr = oldLock.takeIf { it != ProductPropertyLock.NONE }?.asString()
            ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldProductProperty = cache.get(key)
            when {
                oldProductProperty == null -> resultErrorNotFound
                oldProductProperty.lock != oldLockStr -> ProductPropertyResponse.errorConcurrent(
                    oldLock,
                    oldProductProperty.toInternal()
                )

                else -> okBlock(key, oldProductProperty)
            }
        }
    }

    override suspend fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse =
        doUpdate(rq.productProperty.id, rq.productProperty.lock) { key, _ ->
            val newProp = rq.productProperty.copy(lock = ProductPropertyLock(randomUuid()))
            val entity = ProductPropertyEntity(newProp)
            cache.put(key, entity)
            ProductPropertyResponse.success(newProp)
        }

    override suspend fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse =
        doUpdate(rq.id, rq.lock)
        { key, oldProductProperty ->
            val newProp = oldProductProperty.toInternal().copy(deleted = true, lock = ProductPropertyLock(randomUuid()))
            val entity = ProductPropertyEntity(newProp)
            cache.put(key, entity)
            ProductPropertyResponse.success(newProp)
        }

    override suspend fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.descriptionFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.description?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()

        return ProductPropertiesResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = ProductPropertyResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = ProductPropertyResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                PropError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )

        val resultErrorEmptyIds = ProductPropertiesResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    code = "ids-empty",
                    group = "validation",
                    field = "ids",
                    message = "Ids must not be null or blank"
                )
            )
        )

        val resultsErrorNotFound = ProductPropertiesResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                PropError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )

        val resultErrorEmptyLock = ProductPropertyResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                PropError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
    }
}