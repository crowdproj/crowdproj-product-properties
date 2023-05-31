package com.crowdproj.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.auth.resolveFrontPermissions
import com.crowdproj.marketplace.auth.resolveRelationsTo
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropState

fun ICorAddExecDsl<PropContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == PropState.RUNNING }

    handle {
        propRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                propRepoDone.resolveRelationsTo(principal)
            )
        )

        for (prop in propsRepoDone) {
            prop.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    prop.resolveRelationsTo(principal)
                )
            )
        }
    }
}
