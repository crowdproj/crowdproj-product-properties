package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.fail
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.stubs.PropStubs

fun ICorAddExecDsl<PropContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == PropStubs.DB_ERROR && state == PropState.RUNNING }
    handle {
        fail(
            PropError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}