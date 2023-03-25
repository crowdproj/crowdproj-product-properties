package com.crowdproj.marketplace.app.controller

import PropStub
import com.crowdproj.marketplace.api.v1.apiV1Mapper
import com.crowdproj.marketplace.api.v1.encodeResponse
import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyDeleteRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertyReadRequest
import com.crowdproj.marketplace.api.v1.models.ProductPropertySearchRequest
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
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString

val sessions = mutableSetOf<WebSocketSession>()

suspend fun WebSocketSession.wsHandlerV1() {
    sessions.add(this)

    // Handle init request
    val ctx = PropContext()
    val init = apiV1Mapper.encodeResponse(ctx.toTransportInit())
    outgoing.send(Frame.Text(init))

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = PropContext()

        // Handle without flow destruction
        try {
            val request = apiV1Mapper.decodeFromString<IProductPropertyRequest>(jsonStr)
            context.fromTransport(request)
            context.fillStubResponse(request)

            val result = apiV1Mapper.encodeResponse(context.toTransportProductProperty())

            // If change request, response is sent to everyone
            if (context.isUpdatableCommand()) {
                sessions.filter { it.isActive }
                    .forEach {
                        it.send(Frame.Text(result))
                    }
            } else {
                outgoing.send(Frame.Text(result))
            }
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (t: Throwable) {
            context.addError(t.asPropError())

            val result = apiV1Mapper.encodeResponse(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}

private fun PropContext.fillStubResponse(request: IProductPropertyRequest) {
    when (request) {
        is ProductPropertySearchRequest, is ProductPropertyReadRequest -> this.propertiesResponse = PropStub.getList()
        is ProductPropertyDeleteRequest -> this.propertyResponse = PropStub.getDeleted()
        else -> this.propertyResponse = PropStub.get()
    }
}