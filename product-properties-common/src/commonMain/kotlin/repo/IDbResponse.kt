package com.crowdproj.marketplace.common.repo

import com.crowdproj.marketplace.common.models.PropError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<PropError>
}
