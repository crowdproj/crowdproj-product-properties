package com.crowdproj.marketplace.biz.general

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.errorAdministration
import com.crowdproj.marketplace.common.helpers.fail
import com.crowdproj.marketplace.common.models.PropWorkMode
import com.crowdproj.marketplace.common.repo.IPropRepository

fun CorChainDsl<PropContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        propRepo = when (workMode) {
            PropWorkMode.TEST -> settings.repoTest
            PropWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != PropWorkMode.STUB && propRepo == IPropRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}