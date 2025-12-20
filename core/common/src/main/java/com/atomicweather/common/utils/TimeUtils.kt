package com.atomicweather.common.utils

import java.time.*

object TimeUtils {

    /** OpenWeather returns `dt` as unix seconds UTC, and `city.timezone` as shift seconds from UTC. */
    fun utcSecondsToLocalDate(utcSeconds: Long, timezoneOffsetSeconds: Int): LocalDate {
        val instant = Instant.ofEpochSecond(utcSeconds)
        val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds)
        return instant.atOffset(zoneOffset).toLocalDate()
    }

    fun utcSecondsToLocalDateTime(utcSeconds: Long, timezoneOffsetSeconds: Int): LocalDateTime {
        val instant = Instant.ofEpochSecond(utcSeconds)
        val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds)
        return instant.atOffset(zoneOffset).toLocalDateTime()
    }

    fun minutesBetween(a: LocalDateTime, b: LocalDateTime): Long =
        Duration.between(a, b).toMinutes().let { kotlin.math.abs(it) }
}