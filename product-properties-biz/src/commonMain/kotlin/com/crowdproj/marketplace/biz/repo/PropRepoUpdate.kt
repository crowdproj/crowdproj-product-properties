package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.repo.ProductPropertyRequest

fun CorChainDsl<PropContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertyRequest(propRepoPrepare)
        val result = propRepo.updateProductProperty(request)
        val resultProperty = result.data
        if (result.isSuccess && resultProperty != null) {
            propRepoDone = resultProperty
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}