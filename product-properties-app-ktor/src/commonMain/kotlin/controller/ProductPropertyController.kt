package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.logging.common.IPropLogWrapper
import io.ktor.server.application.*

suspend fun ApplicationCall.createProductProperty(
    appSettings: PropAppSettings,
    loggerProductProperty: IPropLogWrapper
) = processV1<ProductPropertyCreateRequest, ProductPropertyCreateResponse>(
    appSettings,
    loggerProductProperty,
    "product-property-create",
    PropCommand.CREATE
)

suspend fun ApplicationCall.readProductProperties(
    appSettings: PropAppSettings,
    loggerProductProperty: IPropLogWrapper
) = processV1<ProductPropertyReadRequest, ProductPropertyReadResponse>(
    appSettings,
    loggerProductProperty,
    "product-properties-read",
    PropCommand.READ
)

suspend fun ApplicationCall.updateProductProperty(
    appSettings: PropAppSettings,
    loggerProductProperty: IPropLogWrapper
) = processV1<ProductPropertyUpdateRequest, ProductPropertyUpdateResponse>(
    appSettings,
    loggerProductProperty,
    "product-property-update",
    PropCommand.UPDATE
)

suspend fun ApplicationCall.deleteProductProperty(
    appSettings: PropAppSettings,
    loggerProductProperty: IPropLogWrapper
) = processV1<ProductPropertyDeleteRequest, ProductPropertyDeleteResponse>(
    appSettings,
    loggerProductProperty,
    "product-property-delete",
    PropCommand.DELETE
)

suspend fun ApplicationCall.searchProductProperty(
    appSettings: PropAppSettings,
    loggerProductProperty: IPropLogWrapper
) = processV1<ProductPropertySearchRequest, ProductPropertySearchResponse>(
    appSettings,
    loggerProductProperty,
    "product-property-search",
    PropCommand.SEARCH
)