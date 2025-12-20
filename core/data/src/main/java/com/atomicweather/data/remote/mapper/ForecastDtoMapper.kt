package com.atomicweather.data.remote.mapper

import com.atomicweather.data.remote.dto.*
import com.atomicaweather.domain.model.*
import javax.inject.Inject

class ForecastDtoToDomainMapper @Inject constructor() {

    fun map(dto: ForecastResponseDto): Forecast {
        return Forecast(
            list = dto.list.map { it.toDomain() },
            city = dto.city?.toDomain()
        )
    }

    private fun CityDto.toDomain(): City {
        return City(
            name = name,
            timezone = timezone
        )
    }

    private fun ForecastItemDto.toDomain(): ForecastItem {
        return ForecastItem(
            dt = dt,
            main = main?.toDomain(),
            weather = weather.map { it.toDomain() }
        )
    }

    private fun MainDto.toDomain(): Main {
        return Main(
            tempMin = tempMin,
            tempMax = tempMax,
            temp = temp
        )
    }

    private fun WeatherDto.toDomain(): Weather {
        return Weather(
            main = main,
            description = description,
            icon = icon
        )
    }
}