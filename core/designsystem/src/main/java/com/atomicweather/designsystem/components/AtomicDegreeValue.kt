package com.atomicweather.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.atomicweather.designsystem.theme.LocalAppTypography

@Composable
fun AtomicDegreeValue(
    tempValue: String,
    textStyle: TextStyle = LocalAppTypography.current.weatherTemperature
) {
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
        text = annotated,
        style = textStyle,
        color = MaterialTheme.colorScheme.onPrimary
    )
}