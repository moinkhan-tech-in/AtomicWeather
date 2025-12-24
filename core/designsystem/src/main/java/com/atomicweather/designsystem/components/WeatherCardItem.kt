package com.atomicweather.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicweather.designsystem.theme.AtomicWeatherTheme
import com.atomicweather.designsystem.theme.LocalAppTypography

@Composable
fun WeatherCardItem(
    title: String,
    leadingImageSpec: AtomicImageSpec,
    tempValue: String,
    isExpanded: Boolean = false,
    onExpandState: (Boolean) -> Unit = {},
    detail: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = { onExpandState(isExpanded.not()) })
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            style = LocalAppTypography.current.weatherCardTitle,
            text = title,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AtomicImage(
                modifier = Modifier.size(64.dp),
                spec = leadingImageSpec
            )

            AtomicDegreeValue(tempValue = tempValue)
        }

        AnimatedVisibility(isExpanded) {
            Column { detail.invoke(this) }
        }

        LaunchedEffect(isExpanded) {
            onExpandState(isExpanded)
        }
    }
}

@Composable
fun WeatherCardSubItem(
    leadingImageSpec: AtomicImageSpec,
    time: String,
    tempValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AtomicImage(
            modifier = Modifier.size(36.dp),
            spec = leadingImageSpec
        )
        Text(
            text = time,
            style = LocalAppTypography.current.weatherCardBody
        )
        AtomicDegreeValue(
            tempValue = tempValue,
            textStyle = LocalAppTypography.current.weatherTemperatureMini
        )
    }
}

@Composable
@Preview
private fun WeatherCardItemPreview() {
    AtomicWeatherTheme {
        WeatherCardItem(
            title = "Monday",
            leadingImageSpec = AtomicImageSpec.Res(0),
            tempValue = "27°C"
        )
    }
}