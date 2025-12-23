package com.atomicweather.feature.weather.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.atomicweather.designsystem.components.AtomicAlertDialog
import com.atomicweather.feature.weather.R

// Define a state class to hold the launcher function
class LocationPermissionState(
    val launchPermissionRequest: () -> Unit
)

@Composable
fun rememberLocationPermissionState(
    onPermissionResult: (isGranted: Boolean) -> Unit
): LocationPermissionState {
    val context = LocalContext.current
    val activity = context.findActivity()

    val permissions = remember {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun isLocationGranted(): Boolean {
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return coarse || fine
    }

    var showRationaleDialog by remember { mutableStateOf(false) }
    var showGoToSettingsDialog by remember { mutableStateOf(false) }

    // This launcher handles the result of the permission request
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val isGranted = result.values.any { it }
        onPermissionResult(isGranted)

        if (!isGranted) {
            val shouldShowRationale = permissions.any { perm ->
                ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
            }
            if (!shouldShowRationale) {
                // User checked "Don't ask again"
                showGoToSettingsDialog = true
            }
        }
    }

    // --- UI dialogs for rationale and settings ---

    if (showRationaleDialog) {
        AtomicAlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = stringResource(R.string.allow_location_access),
            text = stringResource(R.string.we_use_your_location_to_show_the_weather_forecast_for_your_area),
            confirmButtonText = stringResource(R.string.allow),
            dismissButtonText = stringResource(R.string.not_now),
            confirmButtonClick = {
                showRationaleDialog = false
                launcher.launch(permissions)
            },
            dismissButtonClick = { showRationaleDialog = false }
        )
    }

    if (showGoToSettingsDialog) {
        AtomicAlertDialog(
            onDismissRequest = { showGoToSettingsDialog = false },
            title = stringResource(R.string.location_permission_needed),
            text = stringResource(R.string.location_permission_is_disabled_enable_it_from_settings_to_get_local_weather),
            confirmButtonText = stringResource(R.string.open_settings),
            dismissButtonText = stringResource(R.string.cancel),
            confirmButtonClick = {
                showGoToSettingsDialog = false
                context.openAppSettings()
            },
            dismissButtonClick = { showGoToSettingsDialog = false }
        )
    }

    // Return the state object with the launch function
    return remember {
        LocationPermissionState(
            launchPermissionRequest = {
                if (isLocationGranted()) {
                    onPermissionResult(true)
                } else {
                    val shouldShowRationale = permissions.any { perm ->
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
                    }
                    if (shouldShowRationale) {
                        showRationaleDialog = true
                    } else {
                        launcher.launch(permissions)
                    }
                }
            }
        )
    }
}


// -------- helpers --------

private fun Context.findActivity(): Activity {
    var ctx: Context = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    error("LocationPermissionHandler must be used in an Activity context.")
}

private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
