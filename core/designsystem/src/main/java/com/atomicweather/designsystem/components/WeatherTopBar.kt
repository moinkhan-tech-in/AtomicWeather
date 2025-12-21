package com.atomicweather.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.atomicweather.designsystem.theme.LocalAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopBar(
    title: String,
    trailingImgSpec: AtomicImageSpec? = null,
    trailingIconClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            title = {
                Text(
                    style = LocalAppTypography.current.title,
                    text = title,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                trailingImgSpec?.let {
                    AtomicImage(
                        modifier = Modifier
                            .clickable(enabled = true) { trailingIconClick() }
                            .size(30.dp),
                        spec = it
                    )
                    Spacer(Modifier.size(12.dp))
                }
            }
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}