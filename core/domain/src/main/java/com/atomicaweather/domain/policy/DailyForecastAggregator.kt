package com.atomicaweather.domain.policy

import com.atomicaweather.domain.model.Forecast


interface DailyForecastAggregator {
    fun aggregate(): Forecast
}