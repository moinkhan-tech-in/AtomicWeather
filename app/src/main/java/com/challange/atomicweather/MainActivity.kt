package com.challange.atomicweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atomicweather.feature.weather.main.WeatherMainScreen
import com.challange.atomicweather.ui.theme.AtomicWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtomicWeatherTheme {
                WeatherMainScreen()
            }
        }
    }
}