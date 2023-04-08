package com.crowdproj.marketplace.api.logs.mapper

import com.crowdproj.marketplace.api.logs.models.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.models.*
import kotlinx.datetime.Clock

fun PropContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "crowdproj-product-property",
    productProperty = toMkplLog(),
    errors = errors.map { it.toLog() },
)

fun PropContext.toMkplLog(): PropLogModel? {
    val propBlank = ProductProperty()
    return PropLogModel(
        requestId = requestId.takeIf { it != PropRequestId.NONE }?.asString(),
        operation = command.takeIf { it != PropCommand.NONE }?.name?.let { PropLogModel.Operation.valueOf(it) },
        requestProductProp = propertyRequest.takeIf { it != propBlank }?.toLog(),
        responseProductProp = propertyResponse.takeIf { it != propBlank }?.toLog(),
        requestProductProps = propertiesRequest.takeIf { it.isNotEmpty() }
            ?.filter { it != propBlank }
            ?.map { it.toLog() },
        responseProductProps = propertiesResponse.takeIf { it.isNotEmpty() }
            ?.filter { it != propBlank }
            ?.map { it.toLog() },
        requestFilter = propertiesFilterRequest.takeIf { it != ProductPropertyFilter() }?.toLog(),
    ).takeIf { it != PropLogModel() }
}

fun PropError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun ProductProperty.toLog() = ProductPropLog(
    id = id.takeIf { it != ProductPropertyId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    unitMain = unitMain.takeIf { it != ProductUnitId.NONE }?.asString(),
    units = units.filter { it != ProductUnitId.NONE }.map { it.asString() },
    deleted = deleted
)

private fun ProductPropertyFilter.toLog() = ProductPropFilterLog(
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() }
)
