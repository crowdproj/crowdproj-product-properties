package com.crowdproj.marketplace.api.v1

import kotlinx.serialization.json.Json

val apiV1Mapper = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}