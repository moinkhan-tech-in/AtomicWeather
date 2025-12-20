package com.atomicaweather.domain.policy

import com.atomicaweather.domain.model.Forecast
import com.atomicweather.common.utils.TimeUtils
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultDailyForecastAggregator @Inject constructor() : DailyForecastAggregator {

    override fun aggregate(): Forecast {
        return Forecast()
    }
}