package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.fail
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.stubs.PropStubs

fun ICorAddExecDsl<PropContext>.stubValidationBadName(title: String) = worker {
    this.title = title
    on { stubCase == PropStubs.BAD_NAME && state == PropState.RUNNING }
    handle {
        fail(
            PropError(
                group = "validation",
                code = "validation-name",
                field = "name",
                message = "Wrong name field"
            )
        )
    }
}