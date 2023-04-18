package com.crowdproj.marketplace.app.controller

import PropStub
import apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.CwpProdPropContext
import com.crowdproj.marketplace.mappers.v1.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

suspend fun ApplicationCall.createProductProperty() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyCreateRequest>(receiveText())
    val context = CwpProdPropContext()
    context.fromTransport(request)
    respond(apiV1Mapper.encodeToString(context.copy(propertyResponse = PropStub.length).toTransportCreate()))
}

suspend fun ApplicationCall.readProductProperties() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyReadRequest>(receiveText())
    val context = CwpProdPropContext()
    context.fromTransport(request)
    respond(apiV1Mapper.encodeToString(context.copy(propertiesResponse = PropStub.properties).toTransportRead()))
}

suspend fun ApplicationCall.updateProductProperty() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyUpdateRequest>(receiveText())
    val context = CwpProdPropContext()
    context.fromTransport(request)
    respond(apiV1Mapper.encodeToString(context.copy(propertyResponse = PropStub.length).toTransportUpdate()))
}

suspend fun ApplicationCall.deleteProductProperty(){
    val request = apiV1Mapper.decodeFromString<ProductPropertyDeleteRequest>(receiveText())
    val context = CwpProdPropContext()
    context.fromTransport(request)
    respond(apiV1Mapper.encodeToString(context.copy(propertyResponse = PropStub.height) .toTransportDelete()))
}

suspend fun ApplicationCall.searchProductProperty() {
    val request = apiV1Mapper.decodeFromString<ProductPropertySearchRequest>(receiveText())
    val context = CwpProdPropContext()
    context.fromTransport(request)
    respond(apiV1Mapper.encodeToString(context.copy(propertiesResponse = PropStub.properties).toTransportSearch()))
}