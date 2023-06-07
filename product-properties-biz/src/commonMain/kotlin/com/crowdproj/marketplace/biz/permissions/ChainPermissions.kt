package com.crowdproj.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.auth.resolveChainPermissions
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState


fun ICorAddExecDsl<PropContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == PropState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}
