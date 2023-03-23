package com.crowdproj.marketplace.api.v1.request

import com.crowdproj.marketplace.api.v1.IApiStrategy
import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest

sealed interface IRequestStrategy : IApiStrategy<IProductPropertyRequest> {
    companion object {
        private val members: List<IRequestStrategy> = listOf(
            CreateRequestStrategy,
            ReadRequestStrategy,
            UpdateRequestStrategy,
            DeleteRequestStrategy,
            SearchRequestStrategy
        )
        val membersByDiscriminator = members.associateBy { it.discriminator }
        val membersByClazz = members.associateBy { it.clazz }
    }
}