package com.crowdproj.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState

fun ICorAddExecDsl<PropContext>.validation(block: CorChainDsl<PropContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == PropState.RUNNING }
}

fun ICorAddExecDsl<PropContext>.finishValidation(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        propValidated = propValidating
    }
}

fun ICorAddExecDsl<PropContext>.finishPropsValidation(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        propsValidated = propsValidating
    }
}

fun ICorAddExecDsl<PropContext>.finishPropsFilterValidation(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        propsFilterValidated = propsFilterValidating
    }
}