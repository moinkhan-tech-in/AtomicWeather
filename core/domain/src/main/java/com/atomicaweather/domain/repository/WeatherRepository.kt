package com.atomicaweather.domain.repository

import com.atomicaweather.domain.model.Forecast
import com.atomicweather.common.result.AppResult

interface WeatherRepository {

    suspend fun getWeatherForecast(lat: Double, lon: Double): AppResult<Forecast>

}