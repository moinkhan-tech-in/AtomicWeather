package com.atomicweather.feature.weather.main.mapper

import com.atomicaweather.domain.model.Forecast
import com.atomicweather.common.utils.weatherIconUrl
import com.atomicweather.designsystem.components.AtomicImageSpec
import com.atomicweather.feature.weather.R
import com.atomicweather.feature.weather.main.model.ForecastDayUiModel
import com.atomicweather.feature.weather.main.model.ForecastHourUiModel
import com.atomicweather.feature.weather.main.model.ForecastUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.roundToInt

fun weatherToBackground(main: String?): Int {
    return when (main?.lowercase()) {
        "clear" -> R.drawable.bg_sunny
        "clouds" -> R.drawable.bg_cloudy
        "rain", "drizzle", "thunderstorm" -> R.drawable.bg_rainy
        "snow", "mist", "fog", "haze", "smoke", "dust" -> R.drawable.bg_forest
        else -> R.drawable.bg_forest
    }
}

// Can be written in TimeUtils.
private fun getFormatter(pattern: String, tz: TimeZone, locale: Locale = Locale.getDefault()): SimpleDateFormat {
    return SimpleDateFormat(pattern, locale).apply { timeZone = tz }
}

fun Forecast.toUiModel(): ForecastUiModel {
    val cityName = city?.name.orEmpty()
    val cityTimeZoneId = city?.timezone?.let { offset ->
        val hours = offset / 3600
        val minutes = abs(offset % 3600 / 60)
        val sign = if (offset >= 0) "+" else "-"
        "GMT$sign${"%02d".format(hours)}:${"%02d".format(minutes)}"
    }
    val cityTimeZone = cityTimeZoneId?.let { TimeZone.getTimeZone(it) } ?: TimeZone.getTimeZone("UTC")

    val dayKeyFormat = getFormatter("yyyyMMdd", cityTimeZone)
    val dayTitleFormat = getFormatter("EEEE", cityTimeZone)
    val timeFormat = getFormatter("HH:mm", cityTimeZone)


    val days = list
        .mapNotNull { item ->
            val dt = item.dt ?: return@mapNotNull null
            val utcDate = Date(dt * 1000L)
            utcDate to item
        }
        .groupBy { (date, _) -> dayKeyFormat.format(date) }
        .toSortedMap()
        .values
        .take(5)
        .map { entries ->
            val firstEntryDate = entries.first().first
            val forecastItem = entries.first().second

            ForecastDayUiModel(
                title = dayTitleFormat.format(firstEntryDate),
                temp = forecastItem.main?.temp?.roundToInt().toString(),
                weatherIconSpec = AtomicImageSpec.Url(weatherIconUrl(forecastItem.weather.firstOrNull()?.icon)),
                hourly = entries.map { (date, item) ->
                    ForecastHourUiModel(
                        time = timeFormat.format(date),
                        temp = item.main?.temp?.roundToInt().toString(),
                        weatherIconSpec = AtomicImageSpec.Url(weatherIconUrl(item.weather.firstOrNull()?.icon))
                    )
                }
            )
        }

    return ForecastUiModel(
        cityName = cityName,
        days = days,
        bgImgSpec = AtomicImageSpec.Res(weatherToBackground(list.firstOrNull()?.weather?.firstOrNull()?.main))
    )
}
