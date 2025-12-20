package com.atomicaweather.domain.model

data class Forecast(
    val list: List<ForecastItem> = emptyList(),
    val city: City? = null
)

data class City(
    val name: String? = null,
    val timezone: Int? = null // seconds shift from UTC
)

data class ForecastItem(
    val dt: Long? = null, // unix seconds UTC
    val main: Main? = null,
    val weather: List<Weather> = emptyList()
)

data class Main(
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val temp: Double? = null,
)

data class Weather(
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)