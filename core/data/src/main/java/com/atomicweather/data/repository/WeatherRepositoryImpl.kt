package com.atomicweather.data.repository

import com.atomicaweather.domain.model.Forecast
import com.atomicaweather.domain.policy.DailyForecastAggregator
import com.atomicaweather.domain.repository.WeatherRepository
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import com.atomicweather.common.result.fold
import com.atomicweather.data.remote.api.WeatherApi
import com.atomicweather.data.remote.mapper.ForecastDtoToDomainMapper
import com.atomicweather.network.qualifier.WeatherMapApiKey
import com.atomicweather.network.safeApiCall
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val dtoMapper: ForecastDtoToDomainMapper,
    private val aggregator: DailyForecastAggregator,
    @WeatherMapApiKey private val weatherApiKey: String
): WeatherRepository {

    override suspend fun getWeatherForecast(lat: Double, lon: Double): AppResult<Forecast> {
        return safeApiCall {
            weatherApi.get5Day3HourForecast(lat, lon, weatherApiKey)
        }.fold(
            onSuccess = { dto ->
                val domain = dtoMapper.map(dto)
                if (domain.list.isEmpty()) {
                    AppResult.Error(AppError.EmptyData)
                } else {
                    AppResult.Success(domain)
                }
            },
            onError = { it }
        )
    }
}