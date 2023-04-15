package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.stubs.PropStubs

fun CorChainDsl<PropContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == PropStubs.DB_ERROR && state == PropState.RUNNING }
    handle {
        state = PropState.FAILING
        errors.add(
            PropError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}