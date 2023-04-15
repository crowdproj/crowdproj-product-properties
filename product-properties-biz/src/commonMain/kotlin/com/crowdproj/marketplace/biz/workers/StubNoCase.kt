package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState

fun CorChainDsl<PropContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        state = PropState.FAILING
        errors.add(
            PropError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}