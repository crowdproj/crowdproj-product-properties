package com.crowdproj.marketplace.api.v1.response

import com.crowdproj.marketplace.api.v1.IApiStrategy
import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse

sealed interface IResponseStrategy : IApiStrategy<IProductPropertyResponse> {
    companion object {
        val members = listOf(
            CreateResponseStrategy,
            ReadResponseStrategy,
            UpdateResponseStrategy,
            DeleteResponseStrategy,
            SearchResponseStrategy
        )
        val membersByDiscriminator = members.associateBy { it.discriminator }
        val membersByClazz = members.associateBy { it.clazz }
    }
}