package com.atomicweather.feature.weather.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atomicweather.designsystem.components.AtomicImage
import com.atomicweather.designsystem.components.AtomicImageSpec
import com.atomicweather.designsystem.components.WeatherCardItem
import com.atomicweather.designsystem.components.WeatherTopBar
import com.atomicweather.designsystem.theme.AtomicWeatherTheme
import com.atomicweather.feature.weather.R
import com.atomicweather.feature.weather.main.model.ForecastUiModel

@Composable
fun WeatherMainScreen(
    viewModel: WeatherMainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WeatherMainScreenContent(uiState)
}

@Composable
private fun WeatherMainScreenContent(
    uiState: WeatherMainUiState
) {
    var showGrid by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WeatherTopBar(
                title = stringResource(R.string.title_5_day_forecast),
                trailingImgSpec = AtomicImageSpec.Res(if (showGrid) R.drawable.ic_list else R.drawable.ic_grid),
                trailingIconClick = { showGrid = showGrid.not() }
            )
        }
    ) {
        AnimatedVisibility(
            visible = uiState is WeatherMainUiState.Success,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AtomicImage(
                modifier = Modifier.fillMaxSize(),
                spec = (uiState as WeatherMainUiState.Success).forecast.bgImgSpec,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(uiState) { state ->
                when (state) {
                    is WeatherMainUiState.Error -> WeatherMainErrorContent(state.message)
                    WeatherMainUiState.Loading -> WeatherMainLoadingContent()
                    is WeatherMainUiState.Success -> WeatherMainSuccessContent(state.forecast, showGrid)
                }
            }
        }
    }
}


@Composable
private fun WeatherMainSuccessContent(forecast: ForecastUiModel, showGrid: Boolean) {
    Crossfade(showGrid) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = if (it) 128.dp else 360.dp),
            contentPadding = PaddingValues(32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(forecast.days) { item ->
                WeatherCardItem(
                    title = item.title,
                    leadingImageSpec = item.weatherIconSpec,
                    tempValue = item.hourly.first().temp
                )
            }
        }
    }
}

@Composable
private fun WeatherMainErrorContent(message: String) {
    Text(message)
}

@Composable
private fun WeatherMainLoadingContent() {
    Text("Loading")
}

@Composable
@Preview
private fun WeatherMainScreenLoadingPreview() {
    AtomicWeatherTheme {
        WeatherMainScreenContent(uiState = WeatherMainUiState.Loading)
    }
}

@Composable
@Preview
private fun WeatherMainScreenSuccessPreview() {
    AtomicWeatherTheme {
        WeatherMainScreenContent(
            uiState = WeatherMainUiState.Success(
                ForecastUiModel(
                    cityName = "Dubai",
                    days = emptyList()
                )
            )
        )
    }
}

@Composable
@Preview
private fun WeatherMainScreenErrorPreview() {
    AtomicWeatherTheme {
        WeatherMainScreenContent(
            uiState = WeatherMainUiState.Error("Error")
        )
    }
}