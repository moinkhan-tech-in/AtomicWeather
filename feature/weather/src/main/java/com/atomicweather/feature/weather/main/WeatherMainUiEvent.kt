package com.atomicweather.feature.weather.main

sealed interface WeatherMainUiEvent {
    data object OnPermissionGranted : WeatherMainUiEvent
    data object OnRetry : WeatherMainUiEvent
}