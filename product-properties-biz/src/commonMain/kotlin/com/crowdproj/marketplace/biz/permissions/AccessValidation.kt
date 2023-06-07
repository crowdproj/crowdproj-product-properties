package com.crowdproj.marketplace.biz.permissions

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.auth.checkPermitted
import com.crowdproj.marketplace.auth.resolveRelationsTo
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.fail
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState

fun ICorAddExecDsl<PropContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == PropState.RUNNING }
    worker("Вычисление отношения свойства к принципалу") {
        propRepoRead.principalRelations = propRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к свойству") {
        permitted = checkPermitted(command, propRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(PropError(message = "User is not allowed to perform this operation"))
        }
    }
}

fun ICorAddExecDsl<PropContext>.accessValidationProps(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == PropState.RUNNING }
    worker("Вычисление отношения свойств к принципалу") {
        propsRepoRead.forEach { it.principalRelations = it.resolveRelationsTo(principal) }
    }
    worker("Вычисление доступа к свойствам") {
        propsRepoRead.forEach {
            permitted = checkPermitted(command, it.principalRelations, permissionsChain)
            if (!permitted) return@forEach
        }
        if (propsRepoRead.isEmpty()) permitted = true
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(PropError(message = "User is not allowed to perform this operation"))
        }
    }
}

