package com.challange.atomicweather

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atomicweather.feature.weather.main.WeatherMainScreen
import kotlinx.serialization.Serializable

sealed interface AtomicWeatherNavRoutes {

    @Serializable
    object Main : AtomicWeatherNavRoutes
}

@Composable
fun AtomicNavHost() {
    NavHost(
        navController = rememberNavController(),
        startDestination = AtomicWeatherNavRoutes.Main
    ) {
        composable<AtomicWeatherNavRoutes.Main> {
            WeatherMainScreen()
        }
    }
}