package com.atomicweather.feature.weather.main.model

data class ForecastUiModel(
    val cityName: String,
    val days: List<ForecastDayUiModel>
)

data class ForecastDayUiModel(
    val title: String,              // "Mon"
    val subtitle: String,           // "23 Dec"
    val hourly: List<ForecastHourUiModel>
)

data class ForecastHourUiModel(
    val time: String,               // "15:00"
    val temp: String,               // "27°C"
    val icon: String
)