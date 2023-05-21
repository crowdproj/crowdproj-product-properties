package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.common.repo.ProductPropertyFilterRequest

fun ICorAddExecDsl<PropContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск свойств продукта в БД по фильтру"
    on { state == PropState.RUNNING }
    handle {
        val request = ProductPropertyFilterRequest(
            nameFilter = propsFilterValidated.name,
            descriptionFilter = propValidated.description
        )
        val result = propRepo.searchProductProperty(request)
        val resultProps = result.data
        if (result.isSuccess && resultProps != null) {
            propsRepoDone = resultProps.toMutableList()
        } else {
            state = PropState.FAILING
            errors.addAll(result.errors)
        }
    }
}