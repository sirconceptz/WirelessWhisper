package com.hermanowicz.wirelesswhisper.navigation.features.settings.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.card.CardWhiteBgWithBorder
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.lifecycle.OnLifecycleEvent
import com.hermanowicz.wirelesswhisper.components.switches.SwitchPrimary
import com.hermanowicz.wirelesswhisper.components.text.TextLabel
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffoldLazyColumn
import com.hermanowicz.wirelesswhisper.navigation.features.settings.state.AppSettingsState

@Composable
fun SettingsScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.openDeviceSettings) {
        if (uiState.openDeviceSettings) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            context.startActivity(intent)
            viewModel.openDeviceSettings(false)
        }
    }

    val systemUiController = rememberSystemUiController()
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.observeAppSettings()
            }

            Lifecycle.Event.ON_START -> {
                systemUiController.isStatusBarVisible = true
            }

            else -> Unit
        }
    }

    TopBarScaffoldLazyColumn(
        topBarText = stringResource(id = R.string.settings),
        bottomBar = bottomBar
    ) {
        item {
            TextLabel(text = stringResource(id = R.string.main_settings))
            SettingsCard(uiState, viewModel)
        }
    }
}

@Composable
private fun SettingsCard(
    uiState: AppSettingsState,
    viewModel: SettingsViewModel
) {
    CardWhiteBgWithBorder {
        SwitchPrimary(
            label = stringResource(id = R.string.bluetooth),
            state = uiState.bluetoothEnabled,
            onStateChange = { viewModel.openDeviceSettings(true) }
        )
        DividerCardInside()
        SwitchPrimary(
            label = stringResource(id = R.string.notifications),
            state = uiState.notificationsEnabled,
            onStateChange = { viewModel.openDeviceSettings(true) }
        )
    }
}
