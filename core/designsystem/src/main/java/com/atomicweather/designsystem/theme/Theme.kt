package com.atomicweather.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AtomicWeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalAppTypography provides AtomicWeatherTypography(),
        LocalAppColors provides AtomicWeatherAppColor()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}