package com.crowdproj.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.errorValidation
import com.crowdproj.marketplace.common.helpers.fail

fun ICorAddExecDsl<PropContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { propValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}