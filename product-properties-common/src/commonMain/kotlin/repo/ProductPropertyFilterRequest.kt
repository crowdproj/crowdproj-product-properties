package com.crowdproj.marketplace.common.repo

data class ProductPropertyFilterRequest(
    val nameFilter: String = "",
    val descriptionFilter: String = "",
)