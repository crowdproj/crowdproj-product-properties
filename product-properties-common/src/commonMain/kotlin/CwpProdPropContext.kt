package com.crowdproj.marketplace.common

import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import kotlinx.datetime.Instant

data class CwpProdPropContext(
    val command: PropCommand = PropCommand.NONE,
    val state: PropState = PropState.NONE,
    val errors: List<PropError> = emptyList(),

    val workMode: PropWorkMode = PropWorkMode.PROD,
    val stubCase: PropStubs = PropStubs.NONE,

    val requestId: PropRequestId = PropRequestId.NONE,
    val timeStart: Instant = Instant.NONE,

    val propertyRequest: ProductProperty = ProductProperty(),
    val propertiesRequest: List<ProductProperty> = emptyList(),
    val propertiesFilterRequest: ProductPropertyFilter = ProductPropertyFilter(),

    val propertyResponse: ProductProperty = ProductProperty(),
    val propertiesResponse: List<ProductProperty> = emptyList(),
)