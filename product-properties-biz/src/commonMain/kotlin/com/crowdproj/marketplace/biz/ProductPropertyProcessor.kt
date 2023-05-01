package com.crowdproj.marketplace.biz

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import com.crowdproj.marketplace.biz.general.initRepo
import com.crowdproj.marketplace.biz.general.operation
import com.crowdproj.marketplace.biz.general.prepareResult
import com.crowdproj.marketplace.biz.general.stubs
import com.crowdproj.marketplace.biz.repo.*
import com.crowdproj.marketplace.biz.validation.*
import com.crowdproj.marketplace.biz.workers.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.PropCorSettings
import com.crowdproj.marketplace.common.models.ProductPropertyId
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.models.PropState

class ProductPropertyProcessor(private val settings: PropCorSettings = PropCorSettings()) {
    suspend fun exec(ctx: PropContext) =
        productPropertyChain.exec(ctx.apply { settings = this@ProductPropertyProcessor.settings })

    companion object {
        private val productPropertyChain = rootChain<PropContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")
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
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание свойства продукта в БД")
                }
                prepareResult("Подготовка ответа")
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
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение cвойства продукта из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == PropState.RUNNING }
                        handle { propsRepoDone = propsRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
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
                chain {
                    title = "Логика сохранения"
                    repoReadOne("Чтение cвойства продукта из БД")
                    repoPrepareUpdate("Подготовка cвойства продукта для обновления")
                    repoUpdate("Обновление cвойства продукта в БД")
                }
                prepareResult("Подготовка ответа")
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
                chain {
                    title = "Логика удаления"
                    repoReadOne("Чтение cвойства продукта из БД")
                    repoPrepareDelete("Подготовка cвойства продукта для удаления")
                    repoDelete("Удаление cвойства продукта из БД")
                }
                prepareResult("Подготовка ответа")
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
                repoSearch("Поиск свойств продукта в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}