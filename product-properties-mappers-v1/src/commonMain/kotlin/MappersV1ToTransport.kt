package com.crowdproj.marketplace.mappers.v1

import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.CwpProdPropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.mappers.v1.exceptions.UnknownPropCommand

fun CwpProdPropContext.toTransportProductProperty(): IProductPropertyResponse = when (val cmd = command) {
    PropCommand.CREATE -> toTransportCreate()
    PropCommand.READ -> toTransportRead()
    PropCommand.UPDATE -> toTransportUpdate()
    PropCommand.DELETE -> toTransportDelete()
    PropCommand.SEARCH -> toTransportSearch()
    PropCommand.NONE -> throw UnknownPropCommand(cmd)
}

fun CwpProdPropContext.toTransportCreate() = ProductPropertyCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PropState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    productProperty = propertyResponse.toTransport()
)

fun CwpProdPropContext.toTransportRead() = ProductPropertyReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PropState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    productProperties = propertiesResponse.toTransport()
)

fun CwpProdPropContext.toTransportUpdate() = ProductPropertyUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PropState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    productProperty = propertyResponse.toTransport()
)

fun CwpProdPropContext.toTransportDelete() = ProductPropertyDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PropState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    productProperty = propertyResponse.toTransport()
)

fun CwpProdPropContext.toTransportSearch() = ProductPropertySearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PropState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    productProperties = propertiesResponse.toTransport()
)

private fun ProductProperty.toTransport() = ProductPropertyResponseObject(
    id = id.takeIf { it != ProductPropertyId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    unitMain = unitMain.takeIf { it != ProductUnitId.NONE }?.asString(),
    units = units.mapNotNull { it.takeIf { it != ProductUnitId.NONE }?.asString() }
)

private fun List<PropError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PropError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    title = message.takeIf { it.isNotBlank() },
    description = exception?.message
)

private fun List<ProductProperty>.toTransport(): List<ProductPropertyResponseObject> = this.map { it.toTransport() }