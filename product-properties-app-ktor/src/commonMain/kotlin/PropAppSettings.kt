package com.crowdproj.marketplace.app

import com.crowdproj.marketplace.biz.ProductPropertyProcessor
import com.crowdproj.marketplace.common.PropCorSettings

data class PropAppSettings(
    val appUrls: List<String>,
    val corSettings: PropCorSettings,
    val processor: ProductPropertyProcessor,
)