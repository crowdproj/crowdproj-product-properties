package com.crowdproj.marketplace.biz.validation

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.errorValidation
import com.crowdproj.marketplace.common.helpers.fail

fun CorChainDsl<PropContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { propValidating.description.isNotEmpty() && !propValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain leters"
            )
        )
    }
}