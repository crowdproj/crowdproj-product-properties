package com.crowdproj.marketplace.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.models.PropWorkMode

fun ICorAddExecDsl<PropContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != PropWorkMode.STUB }
    handle {
        propertyResponse = propRepoDone
        propertiesResponse = propsRepoDone
        state = when (val st = state) {
            PropState.RUNNING -> PropState.FINISHING
            else -> st
        }
    }
}