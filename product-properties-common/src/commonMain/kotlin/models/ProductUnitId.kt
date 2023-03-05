package com.crowdproj.marketplace.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ProductUnitId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ProductUnitId("")
    }
}