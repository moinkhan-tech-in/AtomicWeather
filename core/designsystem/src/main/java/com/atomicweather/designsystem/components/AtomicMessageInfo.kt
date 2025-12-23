package com.atomicweather.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atomicweather.designsystem.theme.LocalAppTypography

@Composable
fun AtomicMessageInfo(
    text: String,
    imageSpec: AtomicImageSpec,
    actionText: String,
    onActionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AtomicImage(modifier = Modifier.size(200.dp), spec = imageSpec)
        Text(text = text, color = MaterialTheme.colorScheme.primary, style = LocalAppTypography.current.weatherCardBody)
        AtomicPrimaryButton(text = actionText, onClick = onActionClick)
    }
}