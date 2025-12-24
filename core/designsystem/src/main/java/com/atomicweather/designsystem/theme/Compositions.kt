package com.atomicweather.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppTypography = staticCompositionLocalOf<AtomicWeatherTypography> {
    error("Not provided")
}

val LocalAppColors = staticCompositionLocalOf<AtomicWeatherAppColor> {
    error("Not provided")
}