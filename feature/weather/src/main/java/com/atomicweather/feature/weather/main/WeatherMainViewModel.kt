package com.atomicweather.feature.weather.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicaweather.domain.location.LocationProvider
import com.atomicaweather.domain.usecase.GetWeatherForecastUseCase
import com.atomicweather.common.dispatcher.DispatcherProvider
import com.atomicweather.common.result.fold
import com.atomicweather.feature.weather.main.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(
    val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    val locationProvider: LocationProvider,
    val dispatcher: DispatcherProvider
): ViewModel() {

    private val _uiState = MutableStateFlow<WeatherMainUiState>(WeatherMainUiState.Ideal)
    val uiState: StateFlow<WeatherMainUiState> = _uiState.asStateFlow()

    init {
        fetchLastKnownLocationWeather()
    }

    private fun fetchLastKnownLocationWeather() {
        locationProvider.getCurrentLatLon()
            .filterNotNull()
            .onEach{ fetchWeatherData(it.lat, it.lon) }
            .flowOn(dispatcher.io)
            .launchIn(viewModelScope)
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        _uiState.update { WeatherMainUiState.Loading }

        getWeatherForecastUseCase(lat, lon)
            .flowOn(dispatcher.io)
            .onEach { result ->
                _uiState.update {
                    result.fold(
                        onSuccess = { WeatherMainUiState.Success(it.toUiModel()) },
                        onError = { WeatherMainUiState.Error(it.error.toString()) }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: WeatherMainUiEvent) {
        // Fow now both actions are same.
        when (event) {
            is WeatherMainUiEvent.OnPermissionGranted -> {
                fetchLastKnownLocationWeather()
            }
            is WeatherMainUiEvent.OnRetry -> {
                fetchLastKnownLocationWeather()
            }
        }
    }
}