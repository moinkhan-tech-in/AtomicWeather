package com.atomicweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    val list: List<ForecastItemDto> = emptyList(),
    val city: CityDto? = null
)

@Serializable
data class CityDto(
    val name: String? = null,
    val timezone: Int? = null // seconds shift from UTC
)

@Serializable
data class ForecastItemDto(
    val dt: Long? = null, // unix seconds UTC
    val main: MainDto? = null,
    val weather: List<WeatherDto> = emptyList()
)

@Serializable
data class MainDto(
    @SerialName("temp_min") val tempMin: Double? = null,
    @SerialName("temp_max") val tempMax: Double? = null,
    @SerialName("temp") val temp: Double? = null,
)

@Serializable
data class WeatherDto(
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)