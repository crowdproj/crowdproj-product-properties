package com.crowdproj.marketplace.biz.groups

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.models.PropWorkMode

fun CorChainDsl<PropContext>.stubs(title: String, block: CorChainDsl<PropContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == PropWorkMode.STUB && state == PropState.RUNNING }
}