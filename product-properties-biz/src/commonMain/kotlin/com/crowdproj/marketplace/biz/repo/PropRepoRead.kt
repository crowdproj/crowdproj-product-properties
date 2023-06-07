package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.repo.ProductPropertiesIdsRequest

fun ICorAddExecDsl<PropContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение свойств продукта из БД"
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertiesIdsRequest(propsValidated.map { it.id })
        val result = propRepo.readProductProperties(request)
        val resultProps = result.data
        if (result.isSuccess && resultProps != null) {
            propsRepoRead = resultProps.toMutableList()
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun ICorAddExecDsl<PropContext>.repoReadOne(title: String) = worker {
    this.title = title
    description = "Чтение свойства продукта из БД"
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertiesIdsRequest(listOf(propValidated.id))
        val result = propRepo.readProductProperties(request)
        val resultProps = result.data
        if (result.isSuccess && !resultProps.isNullOrEmpty()) {
            propRepoRead = resultProps.first()
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}