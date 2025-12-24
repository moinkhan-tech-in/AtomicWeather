package com.challange.atomicweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atomicweather.designsystem.components.SetStatusBarForDarkUi
import com.atomicweather.designsystem.theme.AtomicWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtomicWeatherTheme {
                SetStatusBarForDarkUi()
                AtomicNavHost()
            }
        }
    }
}
