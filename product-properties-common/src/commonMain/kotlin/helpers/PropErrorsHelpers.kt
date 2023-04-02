package com.crowdproj.marketplace.common.helpers

import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.PropError

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