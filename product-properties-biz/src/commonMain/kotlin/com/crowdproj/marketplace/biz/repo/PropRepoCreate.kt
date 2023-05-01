package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.repo.ProductPropertyRequest

fun CorChainDsl<PropContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление свойства продукта в БД"
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertyRequest(propRepoPrepare)
        val result = propRepo.createProductProperty(request)
        val resultProp = result.data
        if (result.isSuccess && resultProp != null) {
            propRepoDone = resultProp
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}