package com.crowdproj.marketplace.biz.groups

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.models.PropState

fun CorChainDsl<PropContext>.operation(
    title: String,
    command: PropCommand,
    block: CorChainDsl<PropContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == PropState.RUNNING }
}