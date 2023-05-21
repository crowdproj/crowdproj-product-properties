package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.api.logs.mapper.toLog
import com.crowdproj.marketplace.api.v1.decodeRequest
import com.crowdproj.marketplace.api.v1.encodeResponse
import com.crowdproj.marketplace.app.PropAppSettings
import com.crowdproj.marketplace.common.PropContext
import com.crowdproj.marketplace.common.helpers.addError
import com.crowdproj.marketplace.common.helpers.asPropError
import com.crowdproj.marketplace.common.helpers.isUpdatableCommand
import com.crowdproj.marketplace.mappers.v1.fromTransport
import com.crowdproj.marketplace.mappers.v1.toTransportInit
import com.crowdproj.marketplace.mappers.v1.toTransportProductProperty
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

val sessions = mutableSetOf<WebSocketSession>()

private val clazzWS = WebSocketSession::wsHandlerV1::class.qualifiedName ?: "wsHandlerV1_"

suspend fun WebSocketSession.wsHandlerV1(appSettings: PropAppSettings) {
    sessions.add(this)

    // Handle init request
    val ctx = PropContext()
    val init = encodeResponse(ctx.toTransportInit())
    outgoing.send(Frame.Text(init))

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = PropContext()

        // Handle without flow destruction
        try {
            val request = decodeRequest(jsonStr)

            val logId = clazzWS
            val logger = appSettings.corSettings.loggerProvider.logger(logId)
            logger.doWithLogging(logId) {
                context.fromTransport(request)

                logger.info(
                    msg = "${context.command} request is got",
                    data = context.toLog("${logId}-request"),
                )

                appSettings.processor.exec(context)

                val result = encodeResponse(context.toTransportProductProperty())

                // If change request, response is sent to everyone
                if (context.isUpdatableCommand()) {
                    sessions.forEach {
                        it.send(Frame.Text(result))
                    }
                } else {
                    outgoing.send(Frame.Text(result))
                }

                logger.info(
                    msg = "${context.command} response is sent",
                    data = context.toLog("${logId}-response")
                )
            }
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (t: Throwable) {
            context.addError(t.asPropError())

            val result = encodeResponse(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()

    sessions.remove(this)
}