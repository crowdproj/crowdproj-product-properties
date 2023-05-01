package com.crowdproj.marketplace.repository.inmemory.model

import com.crowdproj.marketplace.common.models.ProductProperty
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId

data class ProductPropertyEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val units: List<String> = emptyList(),
    val unitMain: String? = null,
    val deleted: Boolean = false,
) {
    constructor(model: ProductProperty) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        units = model.units.filter { it != ProductUnitId.NONE }.map { it.asString() },
        unitMain = model.unitMain.takeIf { it != ProductUnitId.NONE }?.asString(),
        deleted = model.deleted,
    )

    fun toInternal() = ProductProperty(
        id = id?.let { ProductPropertyId(it) } ?: ProductPropertyId.NONE,
        name = name.orEmpty(),
        description = description.orEmpty(),
        unitMain = unitMain?.let { ProductUnitId(it) } ?: ProductUnitId.NONE,
        units = units.map { ProductUnitId(it) },
        deleted = deleted
    )
}