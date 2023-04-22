package com.crowdproj.marketplace.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ProductPropertyId(private val id: String) {
    fun asString() = id

    fun checkFormat(): Boolean {
        val regExp = Regex("^[0-9a-zA-Z-]+$")
        return this != NONE && !this.asString().matches(regExp)
    }

    companion object {
        val NONE = ProductPropertyId("")
    }
}