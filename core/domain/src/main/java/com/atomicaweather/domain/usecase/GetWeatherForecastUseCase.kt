package com.atomicaweather.domain.usecase

import com.atomicaweather.domain.model.Forecast
import com.atomicaweather.domain.repository.WeatherRepository
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<AppResult<Forecast>> {
        return flow { emit(weatherRepository.getWeatherForecast(lat, lon)) }
    }
}