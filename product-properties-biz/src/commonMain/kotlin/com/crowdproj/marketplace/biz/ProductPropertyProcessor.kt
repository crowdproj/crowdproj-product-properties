package com.crowdproj.marketplace.biz

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import com.crowdproj.marketplace.biz.groups.operation
import com.crowdproj.marketplace.biz.groups.stubs
import com.crowdproj.marketplace.biz.validation.*
import com.crowdproj.marketplace.biz.workers.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.ProductPropertyId
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
                validation {
                    worker("Копируем поля в propValidating") { propValidating = propertyRequest.deepCopy() }
                    worker("Очистка id") { propValidating.id = ProductPropertyId.NONE }
                    worker("Очистка заголовка") { propValidating.name = propValidating.name.trim() }
                    worker("Очистка описания") { propValidating.description = propValidating.description.trim() }
                    validateNameNotEmpty("Проверка, что наименование не пустое")
                    validateNameHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")
                    finishValidation("Завершение проверок")
                }
            }
            operation("Получить cвойства продукта", PropCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешного получения")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в propsValidating") {
                        propsValidating = propertiesRequest.map { it.deepCopy() }
                    }
                    worker("Очистка id") {
                        propsValidating.forEach { it.id = ProductPropertyId(it.id.asString().trim()) }
                    }
                    validateIdsNotEmpty("Проверка на непустой ids")
                    validateIdsProperFormat("Проверка формата ids")
                    finishPropsValidation("Завершение проверок")
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
                validation {
                    worker("Копируем поля в propValidating") { propValidating = propertyRequest.deepCopy() }
                    worker("Очистка id") { propValidating.id = ProductPropertyId(propValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { propValidating.name = propValidating.name.trim() }
                    worker("Очистка описания") { propValidating.description = propValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateNameNotEmpty("Проверка, что наименование не пустое")
                    validateNameHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")
                    finishValidation("Завершение проверок")
                }
            }
            operation("Удалить cвойство продукта", PropCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешного удаления")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в propValidating") {
                        propValidating = propertyRequest.deepCopy()
                    }
                    worker("Очистка id") { propValidating.id = ProductPropertyId(propValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Поиск cвойств продукта", PropCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешного поиска")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в propsFilterValidating") {
                        propsFilterValidating = propertiesFilterRequest.copy()
                    }

                    finishPropsFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}