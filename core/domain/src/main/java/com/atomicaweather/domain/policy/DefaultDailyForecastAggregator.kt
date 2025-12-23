package com.atomicaweather.domain.policy

import com.atomicaweather.domain.model.Forecast
import javax.inject.Inject

class DefaultDailyForecastAggregator @Inject constructor() : DailyForecastAggregator {

    override fun aggregate(): Forecast {
        return Forecast()
    }
}