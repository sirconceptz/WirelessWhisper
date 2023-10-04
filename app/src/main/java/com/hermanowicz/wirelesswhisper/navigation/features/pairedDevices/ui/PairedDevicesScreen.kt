package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffoldLazyColumn
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun PairedDevicesScreen(
    onClickPairedDevice: (String) -> Unit,
    navigateToToScanDevices: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: PairedDevicesViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    TopBarScaffoldLazyColumn(
        topBarText = stringResource(id = R.string.paired_devices),
        actions = {
            Button(
                modifier = Modifier.padding(end = LocalSpacing.current.small),
                onClick = {
                    navigateToToScanDevices()
                }
            ) {
                Text(text = stringResource(id = R.string.pair_new_device))
            }
        },
        bottomBar = bottomBar
    ) {
        item {
            uiState.deviceList.forEach { device ->
                CardPrimary {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickPairedDevice(device.macAddress) },
                        text = device.name,
                        style = TextStyle.Default.copy(
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}
