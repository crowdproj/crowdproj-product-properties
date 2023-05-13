package com.crowdproj.marketplace.api.v1

import com.crowdproj.marketplace.api.v1.models.IProductPropertyRequest
import com.crowdproj.marketplace.api.v1.models.IProductPropertyResponse
import kotlinx.serialization.json.Json

fun encodeRequest(request: IProductPropertyRequest): String =
    Json.encodeToString(IProductPropertyRequest.serializer(), request)

fun decodeRequest(jsonStr: String): IProductPropertyRequest =
    Json.decodeFromString(IProductPropertyRequest.serializer(), jsonStr)

fun encodeResponse(response: IProductPropertyResponse): String =
    Json.encodeToString(IProductPropertyResponse.serializer(), response)

fun decodeResponse(jsonStr: String): IProductPropertyResponse =
    Json.decodeFromString(IProductPropertyResponse.serializer(), jsonStr)