package com.atomicweather.feature.weather.main.mapper

import com.atomicaweather.domain.model.Forecast
import com.atomicweather.designsystem.components.AtomicImageSpec
import com.atomicweather.feature.weather.R
import com.atomicweather.feature.weather.main.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.roundToInt

private fun weatherIconUrl(iconCode: String?): String {
    return "https://openweathermap.org/img/wn/${iconCode ?: "02d"}@2x.png"
}

fun weatherToBackground(main: String?): Int {
    return when (main?.lowercase()) {
        "clear" -> R.drawable.bg_sunny
        "clouds" -> R.drawable.bg_cloudy
        "rain", "drizzle", "thunderstorm" -> R.drawable.bg_rainy
        "snow", "mist", "fog", "haze", "smoke", "dust" -> R.drawable.bg_forest
        else -> R.drawable.bg_forest
    }
}

private fun getFormatter(pattern: String, tz: TimeZone, locale: Locale = Locale.getDefault()): SimpleDateFormat {
    return SimpleDateFormat(pattern, locale).apply { timeZone = tz }
}

fun Forecast.toUiModel(): ForecastUiModel {
    val cityName = city?.name.orEmpty()
    val cityTimeZoneId = city?.timezone?.let { offset ->
        // Create a TimeZone ID from the seconds offset, e.g., "GMT+05:30"
        val hours = offset / 3600
        val minutes = abs(offset % 3600 / 60)
        val sign = if (offset >= 0) "+" else "-"
        "GMT$sign${"%02d".format(hours)}:${"%02d".format(minutes)}"
    }
    // Fallback to UTC if the city timezone is not available
    val cityTimeZone = cityTimeZoneId?.let { TimeZone.getTimeZone(it) } ?: TimeZone.getTimeZone("UTC")

    // Define the formatters inside the function, configured with the city's specific timezone
    val dayKeyFormat = getFormatter("yyyyMMdd", cityTimeZone)
    val dayTitleFormat = getFormatter("EEEE", cityTimeZone)
    val daySubtitleFormat = getFormatter("dd MMM", cityTimeZone)
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
                subtitle = daySubtitleFormat.format(firstEntryDate),
                weatherIconSpec = AtomicImageSpec.Url(weatherIconUrl(forecastItem.weather.firstOrNull()?.icon)),
                hourly = entries.map { (date, item) ->
                    ForecastHourUiModel(
                        time = timeFormat.format(date),
                        temp = item.main?.temp?.roundToInt().toString(),
                        iconSpec = AtomicImageSpec.Url(weatherIconUrl(item.weather.firstOrNull()?.icon))
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
