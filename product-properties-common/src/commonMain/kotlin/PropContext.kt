package com.crowdproj.marketplace.common

import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import kotlinx.datetime.Instant

data class PropContext(
    var command: PropCommand = PropCommand.NONE,
    var state: PropState = PropState.NONE,
    val errors: MutableList<PropError> = mutableListOf(),
    var settings: PropCorSettings = PropCorSettings.NONE,

    var workMode: PropWorkMode = PropWorkMode.PROD,
    var stubCase: PropStubs = PropStubs.NONE,

    var requestId: PropRequestId = PropRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var propertyRequest: ProductProperty = ProductProperty(),
    var propertiesRequest: MutableList<ProductProperty> = mutableListOf(),
    var propertiesFilterRequest: ProductPropertyFilter = ProductPropertyFilter(),

    var propertyResponse: ProductProperty = ProductProperty(),
    var propertiesResponse: MutableList<ProductProperty> = mutableListOf(),
)