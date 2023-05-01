package com.crowdproj.marketplace.repository.inmemory

import com.benasher44.uuid.uuid4
import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.repo.*
import com.crowdproj.marketplace.repository.inmemory.model.ProductPropertyEntity
import io.github.reactivecircus.cache4k.Cache
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

    override fun createProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        val key = randomUuid()
        val productProperty = rq.productProperty.copy(id = ProductPropertyId(key))
        val entity = ProductPropertyEntity(productProperty)
        cache.put(key, entity)
        return ProductPropertyResponse(
            data = productProperty,
            isSuccess = true,
        )
    }

    override fun readProductProperties(rq: ProductPropertiesIdsRequest): ProductPropertiesResponse {
        val keys = rq.ids.filter { it != ProductPropertyId.NONE }.map { it.asString() }

        if (keys.isEmpty()) return resultErrorEmptyIds

        val propsFound = keys.mapNotNull { cache.get(it) }
        if (propsFound.isEmpty()) return resultsErrorNotFound

        return ProductPropertiesResponse(
            data = propsFound.map { it.toInternal() },
            isSuccess = true
        )
    }

    override fun updateProductProperty(rq: ProductPropertyRequest): ProductPropertyResponse {
        val key = rq.productProperty.id.takeIf { it != ProductPropertyId.NONE }?.asString() ?: return resultErrorEmptyId
        val newProductProperty = rq.productProperty.copy()
        val entity = ProductPropertyEntity(newProductProperty)
        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                ProductPropertyResponse(
                    data = newProductProperty,
                    isSuccess = true,
                )
            }
        }
    }

    override fun deleteProductProperty(rq: ProductPropertyIdRequest): ProductPropertyResponse {
        val key = rq.id.takeIf { it != ProductPropertyId.NONE }?.asString() ?: return resultErrorEmptyId

        return when (val deletingEntity = cache.get(key)?.copy(deleted = true)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, deletingEntity)
                ProductPropertyResponse(
                    data = deletingEntity.toInternal(),
                    isSuccess = true,
                )
            }
        }
    }

    override fun searchProductProperty(rq: ProductPropertyFilterRequest): ProductPropertiesResponse {
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
    }
}