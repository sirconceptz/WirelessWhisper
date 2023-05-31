package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPrimary
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold

@Composable
fun ScanDevicesScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: ScanDevicesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.showDialogOnPairDevice) {
        DialogPrimary(onDismissRequest = { viewModel.showDialogOnPairDevice(false) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Do you want to pair this device?")
                ButtonPrimary(text = "Yes", onClick = { viewModel.onPairDevice() })
            }
        }
    }

    TopBarScaffold(
        topBarText = stringResource(id = R.string.scan_devices),
        actions = {
            Text(
                modifier = Modifier.clickable {
                    viewModel.scanDevices()
                },
                text = stringResource(id = R.string.scan),
                color = Color.White
            )
        },
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            state.foundDevices.forEach { device ->
                item {
                    CardPrimary {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onClickFoundDevice(device) },
                            text = device.name,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    ButtonPrimary(
                        text = stringResource(id = R.string.scan),
                        onClick = { viewModel.scanDevices() }
                    )
                }
            }
        }
    }
}
