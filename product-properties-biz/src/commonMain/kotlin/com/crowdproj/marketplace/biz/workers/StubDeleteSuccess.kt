package com.crowdproj.marketplace.biz.workers

import PropStub
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.stubs.PropStubs

fun CorChainDsl<PropContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == PropStubs.SUCCESS && state == PropState.RUNNING }
    handle {
        state = PropState.FINISHING
        propertyResponse = PropStub.getDeleted()
    }
}