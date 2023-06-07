package com.crowdproj.marketplace.common

import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.permissions.PropPrincipalModel
import com.crowdproj.marketplace.common.permissions.PropUserPermissions
import com.crowdproj.marketplace.common.repo.IPropRepository
import com.crowdproj.marketplace.common.stubs.PropStubs
import kotlinx.datetime.Instant

data class PropContext(
    var command: PropCommand = PropCommand.NONE,
    var state: PropState = PropState.NONE,
    val errors: MutableList<PropError> = mutableListOf(),
    var settings: PropCorSettings = PropCorSettings.NONE,

    var workMode: PropWorkMode = PropWorkMode.PROD,
    var stubCase: PropStubs = PropStubs.NONE,

    var principal: PropPrincipalModel = PropPrincipalModel.NONE,
    val permissionsChain: MutableSet<PropUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var propRepo: IPropRepository = IPropRepository.NONE,
    var propRepoRead: ProductProperty = ProductProperty(),
    var propsRepoRead: MutableList<ProductProperty> = mutableListOf(),
    var propRepoPrepare: ProductProperty = ProductProperty(),
    var propsRepoPrepare: MutableList<ProductProperty> = mutableListOf(),
    var propRepoDone: ProductProperty = ProductProperty(),
    var propsRepoDone: MutableList<ProductProperty> = mutableListOf(),

    var requestId: PropRequestId = PropRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var propertyRequest: ProductProperty = ProductProperty(),
    var propertiesRequest: MutableList<ProductProperty> = mutableListOf(),
    var propertiesFilterRequest: ProductPropertyFilter = ProductPropertyFilter(),

    var propertyResponse: ProductProperty = ProductProperty(),
    var propertiesResponse: MutableList<ProductProperty> = mutableListOf(),

    var propValidating: ProductProperty = ProductProperty(),
    var propValidated: ProductProperty = ProductProperty(),
    var propsValidating: List<ProductProperty> = listOf(),
    var propsValidated: List<ProductProperty> = listOf(),
    var propsFilterValidating: ProductPropertyFilter = ProductPropertyFilter(),
    var propsFilterValidated: ProductPropertyFilter = ProductPropertyFilter(),
)