package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.broadcastReceiver.ScanDevicesBroadcastReceiver
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import timber.log.Timber

@Composable
fun ScanDevicesScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: ScanDevicesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.turnOnDiscoverableMode) {
        if (state.turnOnDiscoverableMode) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val btAdapter = bluetoothManager.adapter
            try {
                if(btAdapter.isDiscovering) {
                    btAdapter.cancelDiscovery()
                    Timber.d("Bluetooth: Cancel discovery new devices")
                }
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                context.startActivity(intent)
            }
            catch (e: SecurityException) {
                Timber.e("Bluetooth is not active")
            }
        }
    }

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
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.medium)
        ) {
            if (state.foundDevices.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No found devices",
                        textAlign = TextAlign.Center
                    )
                }
            }
            state.foundDevices.forEach { device ->
                item {
                    CardPrimary {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onClickFoundDevice(device) },
                            text = device.name,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            item {
                DividerCardInside()
                ButtonPrimary(text = stringResource(id = R.string.turn_on_discoverable)) {
                    viewModel.turnOnDiscoverable(true)
                }
            }
        }
    }

    ScanDevicesBroadcastReceiver(
        onDeviceFound = {
            viewModel.setFoundDevice(it)
        },
        onStartDiscovery = { viewModel.onStartScanning() }
    )
}
