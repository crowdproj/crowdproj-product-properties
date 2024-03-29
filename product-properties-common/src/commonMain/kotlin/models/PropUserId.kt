package com.crowdproj.marketplace.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PropUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PropUserId("")
    }
}