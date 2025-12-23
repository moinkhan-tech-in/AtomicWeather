package com.atomicweather.feature.weather.main

import com.atomicweather.feature.weather.main.model.ForecastUiModel

sealed interface WeatherMainUiState {
    data object Ideal : WeatherMainUiState

    data object Loading : WeatherMainUiState

    data class Success(
        val forecast: ForecastUiModel
    ) : WeatherMainUiState

    data class Error(
        val message: String
    ) : WeatherMainUiState
}