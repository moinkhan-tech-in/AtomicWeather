package com.atomicweather.network.serializer

import kotlinx.serialization.json.Json

object JsonProvider {
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }
}