package com.atomicweather.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atomicweather.designsystem.theme.AtomicWeatherTheme
import com.atomicweather.designsystem.theme.LocalAppTypography

@Composable
fun WeatherCardItem(
    title: String,
    leadingImageSpec: AtomicImageSpec,
    tempValue: String
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            style = LocalAppTypography.current.weatherCardTitle,
            text = title,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AtomicImage(
                modifier = Modifier.size(64.dp),
                spec = leadingImageSpec
            )

            val annotated = buildAnnotatedString {
                append(tempValue)
                withStyle(
                    SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 26.sp // smaller looks better
                    )
                ) {
                    append("°")
                }
            }

            Text(
                style = LocalAppTypography.current.weatherTemperature,
                text = annotated
            )
        }
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