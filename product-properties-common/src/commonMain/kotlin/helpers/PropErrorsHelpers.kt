package com.crowdproj.marketplace.common.helpers

import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.exceptions.RepoConcurrencyException
import com.crowdproj.marketplace.common.models.ProductPropertyLock
import com.crowdproj.marketplace.common.models.PropError
import com.crowdproj.marketplace.common.models.PropState

fun Throwable.asPropError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = PropError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun PropContext.addError(vararg error: PropError) = errors.addAll(error)

fun PropContext.fail(error: PropError) {
    addError(error)
    state = PropState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: PropError.Level = PropError.Level.ERROR,
) = PropError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: PropError.Level = PropError.Level.ERROR,
) = PropError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)

fun errorRepoConcurrency(
    expectedLock: ProductPropertyLock,
    actualLock: ProductPropertyLock?,
    exception: Exception? = null,
) = PropError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)