package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.mappers.v1.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

suspend fun ApplicationCall.createProductProperty() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyCreateRequest>(receiveText())
    val context = PropContext()
    context.fromTransport(request)
    context.propertyResponse = PropStub.get()
    respond(apiV1Mapper.encodeToString(context.toTransportCreate()))
}

suspend fun ApplicationCall.readProductProperties() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyReadRequest>(receiveText())
    val context = PropContext()
    context.fromTransport(request)
    context.propertiesResponse = PropStub.getList()
    respond(apiV1Mapper.encodeToString(context.toTransportRead()))
}

suspend fun ApplicationCall.updateProductProperty() {
    val request = apiV1Mapper.decodeFromString<ProductPropertyUpdateRequest>(receiveText())
    val context = PropContext()
    context.fromTransport(request)
    context.propertyResponse = PropStub.get()
    respond(apiV1Mapper.encodeToString(context.toTransportUpdate()))
}

suspend fun ApplicationCall.deleteProductProperty(){
    val request = apiV1Mapper.decodeFromString<ProductPropertyDeleteRequest>(receiveText())
    val context = PropContext()
    context.fromTransport(request)
    context.propertyResponse = PropStub.getDeleted()
    respond(apiV1Mapper.encodeToString(context.toTransportDelete()))
}

suspend fun ApplicationCall.searchProductProperty(){
    val request = apiV1Mapper.decodeFromString<ProductPropertySearchRequest>(receiveText())
    val context = PropContext()
    context.fromTransport(request)
    context.propertiesResponse = PropStub.getList()
    respond(apiV1Mapper.encodeToString(context.toTransportSearch()))
}