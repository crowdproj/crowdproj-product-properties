package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState

fun CorChainDsl<PropContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == PropState.RUNNING }
    handle {
        propRepoRead = propValidated.deepCopy()
        propRepoPrepare = propRepoRead
    }
}
