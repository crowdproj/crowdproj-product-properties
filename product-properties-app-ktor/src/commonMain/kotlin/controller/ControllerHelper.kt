package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.api.logs.mapper.toLog
import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.asPropError
import com.crowdproj.marketplace.common.models.PropCommand
import com.crowdproj.marketplace.common.models.PropState
import com.crowdproj.marketplace.logging.common.IPropLogWrapper
import com.crowdproj.marketplace.mappers.v1.fromTransport
import com.crowdproj.marketplace.mappers.v1.toTransportProductProperty
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

suspend inline fun <reified Q : IProductPropertyRequest, @Suppress("unused") reified R : IProductPropertyResponse> ApplicationCall.processV1(
    appSettings: PropAppSettings,
    logger: IPropLogWrapper,
    logId: String,
    command: PropCommand? = null,
) {
    val ctx = PropContext(
        timeStart = Clock.System.now(),
    )
    val processor = appSettings.processor
    try {
        logger.doWithLogging(id = logId) {
            val request = apiV1Mapper.decodeFromString<Q>(receiveText())
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            processor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(apiV1Mapper.encodeToString(ctx.toTransportProductProperty()))
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
            )
            ctx.state = PropState.FAILING
            ctx.errors.add(e.asPropError())
            processor.exec(ctx)
            respond(apiV1Mapper.encodeToString(ctx.toTransportProductProperty()))
        }
    }
}