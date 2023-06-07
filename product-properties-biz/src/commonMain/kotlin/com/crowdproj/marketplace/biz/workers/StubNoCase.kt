package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.fail
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState

fun ICorAddExecDsl<PropContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        fail(
            PropError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}