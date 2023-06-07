package com.crowdproj.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.errorValidation
import com.crowdproj.marketplace.common.helpers.fail

fun ICorAddExecDsl<PropContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    on { propValidating.id.checkFormat() }
    handle {
        val encodedId = propValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}

fun ICorAddExecDsl<PropContext>.validateIdsProperFormat(title: String) = worker {
    this.title = title

    on { propsValidating.any { it.id.checkFormat() } }
    handle {
        val ids = propsValidating.filter { it.id.checkFormat() }.map { it.id.asString() }
        ids.forEach {
            it.replace("<", "&lt;")
                .replace(">", "&gt;")
        }

        fail(
            errorValidation(
                field = "ids",
                violationCode = "badFormat",
                description = "ids ${ids.joinToString()} must contain only letters and numbers"
            )
        )
    }
}