package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.repo.ProductPropertyIdRequest

fun ICorAddExecDsl<PropContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление свойства продукта из БД по ID"
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertyIdRequest(propRepoPrepare)
        val result = propRepo.deleteProductProperty(request)
        val resultProperty = result.data
        if (result.isSuccess && resultProperty != null) {
            propRepoDone = resultProperty
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}