package com.atomicweather.common.utils

object Constants {
    const val WEATHER_MAP_API_URL = "https://api.openweathermap.org/"
}

fun weatherIconUrl(iconCode: String?): String {
    return "https://openweathermap.org/img/wn/${iconCode ?: "02d"}@2x.png"
}