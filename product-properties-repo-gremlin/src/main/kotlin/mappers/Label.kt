package com.crowdproj.marketplace.repository.gremlin.mappers

import com.crowdproj.marketplace.common.models.ProductProperty

fun ProductProperty.label(): String? = this::class.simpleName
