package com.crowdproj.marketplace.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState

fun CorChainDsl<PropContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == PropState.RUNNING }
    handle {
        propRepoPrepare = propRepoRead.deepCopy().apply {
            this.name = propValidated.name
            description = propValidated.description
            unitMain = propValidated.unitMain
            units = propValidated.units
        }
    }
}