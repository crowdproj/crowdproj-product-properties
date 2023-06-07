package com.crowdproj.marketplace.biz.workers

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState

fun ICorAddExecDsl<PropContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == PropState.NONE }
    handle { state = PropState.RUNNING }
}