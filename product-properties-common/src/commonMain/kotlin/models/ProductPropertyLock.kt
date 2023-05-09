package com.crowdproj.marketplace.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ProductPropertyLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ProductPropertyLock("")
    }
}