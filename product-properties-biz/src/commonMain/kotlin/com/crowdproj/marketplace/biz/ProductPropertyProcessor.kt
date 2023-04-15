package com.crowdproj.marketplace.biz

import com.crowdproj.kotlin.cor.rootChain
import com.crowdproj.marketplace.biz.groups.operation
import com.crowdproj.marketplace.biz.groups.stubs
import com.crowdproj.marketplace.biz.workers.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropCommand

class ProductPropertyProcessor {
    suspend fun exec(context: PropContext) = productPropertyChain.exec(context)

    companion object {
        private val productPropertyChain = rootChain<PropContext> {
            initStatus("Инициализация статуса")
            operation("Создать cвойство продукта", PropCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешного создания")
                    stubValidationBadName("Имитация ошибки валидации наименования")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить cвойства продукта", PropCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешного получения")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Изменить cвойство продукта", PropCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешного обновления")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadName("Имитация ошибки валидации наименования")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Удалить cвойство продукта", PropCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешного удаления")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Поиск cвойств продукта", PropCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешного поиска")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
        }.build()
    }
}