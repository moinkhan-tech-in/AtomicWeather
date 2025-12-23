package com.atomicweather.feature.weather.main.model

import com.atomicweather.designsystem.components.AtomicImageSpec
import com.atomicweather.feature.weather.R

data class ForecastUiModel(
    val cityName: String,
    val days: List<ForecastDayUiModel>,
    val bgImgSpec: AtomicImageSpec = AtomicImageSpec.Res(R.drawable.bg_forest)
)

data class ForecastDayUiModel(
    val title: String,
    val temp: String,
    val weatherIconSpec: AtomicImageSpec,
    val hourly: List<ForecastHourUiModel>
)

data class ForecastHourUiModel(
    val time: String,
    val temp: String,
    val weatherIconSpec: AtomicImageSpec
)