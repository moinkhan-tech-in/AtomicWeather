package com.atomicweather.feature.weather.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicaweather.domain.usecase.GetWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
): ViewModel() {

    init {
        observeForecastData()
    }

    private fun observeForecastData() {
        getWeatherForecastUseCase.invoke(22.toDouble(), 72.toDouble())
            .onEach {
                Log.d("TAG", "observeForecastData: $it")
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}