package com.crowdproj.marketplace.biz.workers

import PropStub
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.ProductUnitId
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.stubs.PropStubs

fun CorChainDsl<PropContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == PropStubs.SUCCESS && state == PropState.RUNNING }
    handle {
        state = PropState.FINISHING
        val stub = PropStub.prepareResult {
            propertyRequest.id.takeIf { it != ProductPropertyId.NONE }?.also { this.id = it }
            propertyRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            propertyRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            propertyRequest.unitMain.takeIf { it != ProductUnitId.NONE }?.also { this.unitMain = it }
            val units = propertyRequest.units.filter { it != ProductUnitId.NONE }
            units.takeIf { it.isNotEmpty() }.also { this.units = units }
        }
        propertyResponse = stub
    }
}