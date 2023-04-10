package com.crowdproj.marketplace.mappers.v1

import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import com.crowdproj.marketplace.common.stubs.PropStubs
import com.crowdproj.marketplace.mappers.v1.exceptions.UnknownRequestClass

fun PropContext.fromTransport(request: IProductPropertyRequest) = when (request) {
    is ProductPropertyCreateRequest -> fromTransport(request)
    is ProductPropertyReadRequest -> fromTransport(request)
    is ProductPropertyUpdateRequest -> fromTransport(request)
    is ProductPropertyDeleteRequest -> fromTransport(request)
    is ProductPropertySearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun PropContext.fromTransport(request: ProductPropertyCreateRequest) = this.copy(
    command = PropCommand.CREATE,
    requestId = request.requestId(),
    propertyRequest = request.productProperty?.toInternal() ?: ProductProperty(),
    workMode = request.debug.transportToWorkMode(),
    stubCase = request.debug.transportToStubCase()
)


fun PropContext.fromTransport(request: ProductPropertyReadRequest) = this.copy(
    command = PropCommand.READ,
    requestId = request.requestId(),
    propertiesRequest = request.productPropertyIds.fromTransport(),
    workMode = request.debug.transportToWorkMode(),
    stubCase = request.debug.transportToStubCase()
)


fun PropContext.fromTransport(request: ProductPropertyUpdateRequest) = this.copy(
    command = PropCommand.DELETE,
    requestId = request.requestId(),
    propertyRequest = request.productProperty?.toInternal() ?: ProductProperty(),
    workMode = request.debug.transportToWorkMode(),
    stubCase = request.debug.transportToStubCase()
)

fun PropContext. fromTransport(request: ProductPropertyDeleteRequest) = this.copy(
    command = PropCommand.DELETE,
    requestId = request.requestId(),
    propertyRequest = request.productPropertyId.toProductPropertyWithId(),
    workMode = request.debug.transportToWorkMode(),
    stubCase = request.debug.transportToStubCase()
)


fun PropContext.fromTransport(request: ProductPropertySearchRequest) = this.copy(
    command = PropCommand.SEARCH,
    requestId = request.requestId(),
    propertiesFilterRequest = request.productPropertyFilter.toInternal(),
    workMode = request.debug.transportToWorkMode(),
    stubCase = request.debug.transportToStubCase()
)

private fun ProductPropertySearchFilter?.toInternal() = ProductPropertyFilter(
    name = this?.name.orEmpty(),
    description = this?.description.orEmpty()
)

private fun ProductPropertyCreateObject?.toInternal() = ProductProperty(
    name = this?.name.orEmpty(),
    description = this?.description.orEmpty(),
    units = this?.units.toInternal(),
    unitMain = this?.unitMain.toUnitid()
)

private fun ProductPropertyUpdateObject?.toInternal() = ProductProperty(
    id = this?.id.toProductPropertyId(),
    name = this?.name.orEmpty(),
    description = this?.description.orEmpty(),
    units = this?.units.toInternal(),
    unitMain = this?.unitMain.toUnitid()
)

private fun List<String>?.fromTransport(): MutableList<ProductProperty> =
    this?.let { productPropertyIds -> productPropertyIds.map { it.toProductPropertyWithId() } }?.toMutableList()
        ?: mutableListOf()

private fun List<String>?.toInternal(): List<ProductUnitId> =
    this?.let { unitIdStr -> unitIdStr.map { it.toUnitid() } } ?: emptyList()

private fun String?.toUnitid() = this?.let { ProductUnitId(it) } ?: ProductUnitId.NONE

private fun IProductPropertyRequest?.requestId() = this?.requestId?.let { PropRequestId(it) } ?: PropRequestId.NONE

private fun String?.toProductPropertyId() = this?.let { ProductPropertyId(it) } ?: ProductPropertyId.NONE

private fun String?.toProductPropertyWithId() = ProductProperty(id = this.toProductPropertyId())

private fun CpBaseDebug?.transportToWorkMode(): PropWorkMode = when (this?.mode) {
    CpRequestDebugMode.PROD -> PropWorkMode.PROD
    CpRequestDebugMode.TEST -> PropWorkMode.TEST
    CpRequestDebugMode.STUB -> PropWorkMode.STUB
    null -> PropWorkMode.PROD
}

private fun CpBaseDebug?.transportToStubCase(): PropStubs = when (this?.stub) {
    CpRequestDebugStubs.SUCCESS -> PropStubs.SUCCESS
    CpRequestDebugStubs.NOT_FOUND -> PropStubs.NOT_FOUND
    CpRequestDebugStubs.BAD_ID -> PropStubs.BAD_ID
    null -> PropStubs.NONE
}