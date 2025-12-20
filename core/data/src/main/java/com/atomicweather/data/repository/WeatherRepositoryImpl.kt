package com.atomicweather.data.repository

import com.atomicaweather.domain.model.Forecast
import com.atomicaweather.domain.policy.DailyForecastAggregator
import com.atomicaweather.domain.repository.WeatherRepository
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import com.atomicweather.data.remote.api.WeatherApi
import com.atomicweather.data.remote.mapper.ForecastDtoToDomainMapper
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val dtoMapper: ForecastDtoToDomainMapper,
    private val aggregator: DailyForecastAggregator
): WeatherRepository {

    override suspend fun getWeatherForecast(lat: Double, lon: Double): AppResult<Forecast> {
        val dto = weatherApi.get5Day3HourForecast(lat, lon, "e53ef1c8445ecce4efc8b90a9d82a96a")
        val domain = dtoMapper.map(dto)
//        aggregator.aggregate(dto.city?.timezone ?: 0, dto.list)
        return AppResult.Success(domain)
    }
}