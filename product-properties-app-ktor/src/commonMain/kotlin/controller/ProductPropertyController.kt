package com.crowdproj.marketplace.app.controller

import PropStub
import com.crowdproj.marketplace.api.logs.mapper.toLog
import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.*
import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.mappers.v1.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

private val clazzCreate = ApplicationCall::createProductProperty::class.qualifiedName ?: "create"
private val clazzRead = ApplicationCall::readProductProperties::class.qualifiedName ?: "read"
private val clazzUpdate = ApplicationCall::updateProductProperty::class.qualifiedName ?: "update"
private val clazzDelete = ApplicationCall::deleteProductProperty::class.qualifiedName ?: "delete"
private val clazzSearch = ApplicationCall::searchProductProperty::class.qualifiedName ?: "search"

suspend fun ApplicationCall.createProductProperty(appSettings: PropAppSettings) {
    val logId = "create"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzCreate)

    logger.doWithLogging(logId) {
        val request = apiV1Mapper.decodeFromString<ProductPropertyCreateRequest>(receiveText())
        val context = PropContext()
        context.fromTransport(request)

        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )

        context.propertyResponse = PropStub.get()
        respond(apiV1Mapper.encodeToString(context.toTransportCreate()))

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.readProductProperties(appSettings: PropAppSettings) {
    val logId = "read"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzRead)

    logger.doWithLogging(logId) {
        val request = apiV1Mapper.decodeFromString<ProductPropertyReadRequest>(receiveText())
        val context = PropContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )

        context.propertiesResponse = PropStub.getList()
        respond(apiV1Mapper.encodeToString(context.toTransportRead()))

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.updateProductProperty(appSettings: PropAppSettings) {
    val logId = "update"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzUpdate)

    logger.doWithLogging(logId) {
        val request = apiV1Mapper.decodeFromString<ProductPropertyUpdateRequest>(receiveText())
        val context = PropContext()
        context.fromTransport(request)

        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )

        context.propertyResponse = PropStub.get()
        respond(apiV1Mapper.encodeToString(context.toTransportUpdate()))

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.deleteProductProperty(appSettings: PropAppSettings) {
    val logId = "delete"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzDelete)

    logger.doWithLogging(logId) {
        val request = apiV1Mapper.decodeFromString<ProductPropertyDeleteRequest>(receiveText())
        val context = PropContext()
        context.fromTransport(request)

        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )

        context.propertyResponse = PropStub.getDeleted()
        respond(apiV1Mapper.encodeToString(context.toTransportDelete()))

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.searchProductProperty(appSettings: PropAppSettings) {
    val logId = "search"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzSearch)

    logger.doWithLogging(logId) {
        val request = apiV1Mapper.decodeFromString<ProductPropertySearchRequest>(receiveText())
        val context = PropContext()
        context.fromTransport(request)

        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )

        context.propertiesResponse = PropStub.getList()
        respond(apiV1Mapper.encodeToString(context.toTransportSearch()))

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}