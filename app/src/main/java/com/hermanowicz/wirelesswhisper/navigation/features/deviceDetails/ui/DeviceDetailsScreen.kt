package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DeviceDetailsScreen(
    bottomBar: @Composable () -> Unit,
    bluetoothService: Intent,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    TopBarScaffold(
        topBarText = stringResource(id = R.string.device_details),
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.medium)
        ) {
            item {
                CardPrimary {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.name),
                            color = Color.White
                        )
                        Text(
                            text = uiState.device?.name ?: "",
                            color = Color.White
                        )
                    }
                    DividerCardInside(
                        modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.mac_address),
                            color = Color.White
                        )
                        Text(
                            text = uiState.device?.macAddress ?: "",
                            color = Color.White
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonPrimary(modifier = Modifier.weight(1f), text = stringResource(id = R.string.connect)) {
                        connectDevice(uiState.device, bluetoothService, context)
                    }
                    Spacer(modifier = Modifier.width(LocalSpacing.current.medium))
                    ButtonPrimary(modifier = Modifier.weight(1f), text = stringResource(id = R.string.disconnect)) {
                        disconnectDevice(uiState.device, bluetoothService, context)
                    }
                }
            }
        }
    }
}

fun connectDevice(device: Device?, bluetoothService: Intent, context: Context) {
    if (device != null) {
        bluetoothService.putExtra(
            MyBluetoothService.ACTION_CONNECT,
            device.macAddress
        )
        bluetoothService.action = MyBluetoothService.ACTION_CONNECT
        context.startService(bluetoothService)
    }
}

fun disconnectDevice(device: Device?, bluetoothService: Intent, context: Context) {
    if (device != null) {
        bluetoothService.action = MyBluetoothService.ACTION_DISCONNECT
        bluetoothService.putExtra(MyBluetoothService.ACTION_DISCONNECT, device.macAddress)
        context.startService(bluetoothService)
    }
}
