package com.atomicweather.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.atomicweather.designsystem.theme.LocalAppTypography

@Composable
fun AtomicAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    confirmButtonClick: () -> Unit,
    dismissButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = LocalAppTypography.current.title
            )
        },
        text = { Text(text, style = LocalAppTypography.current.weatherCardBody) },
        confirmButton = {
            TextButton(onClick = confirmButtonClick) {
                Text(
                    text = confirmButtonText,
                    color = MaterialTheme.colorScheme.primary,
                    style = LocalAppTypography.current.weatherActionText
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismissButtonClick) {
                Text(
                    text = dismissButtonText,
                    color = MaterialTheme.colorScheme.primary,
                    style = LocalAppTypography.current.weatherActionText
                )
            }
        }
    )
}