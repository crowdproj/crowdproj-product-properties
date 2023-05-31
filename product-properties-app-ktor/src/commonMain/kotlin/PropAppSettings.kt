package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.app.base.KtorAuthConfig
import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropCorSettings

data class PropAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: PropCorSettings,
    val processor: ProductPropertyProcessor = ProductPropertyProcessor(settings = corSettings),
    val auth: KtorAuthConfig = KtorAuthConfig.NONE,
)