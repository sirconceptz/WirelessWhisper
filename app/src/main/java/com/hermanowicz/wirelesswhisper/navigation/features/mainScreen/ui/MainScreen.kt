package com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun MainScreen(
    onNavigateToPairedDevices: () -> Unit,
    onNavigateToScanDevices: () -> Unit,
    onNavigateToSettings: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    TopBarScaffold(
        topBarText = stringResource(id = R.string.app_name),
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.medium)
        ) {
            item {
                ButtonPrimary(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToPairedDevices() },
                    text = stringResource(id = R.string.paired_devices)
                )
                ButtonPrimary(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToScanDevices() },
                    text = stringResource(id = R.string.scan_devices)
                )
                ButtonPrimary(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToSettings() },
                    text = stringResource(id = R.string.settings)
                )
            }
        }
    }
}
