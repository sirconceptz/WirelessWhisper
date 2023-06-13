package com.hermanowicz.wirelesswhisper.components.broadcastReceiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

@Composable
fun SystemBroadcastReceiver(
    intentFilter: IntentFilter,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current

    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    DisposableEffect(context) {
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun ScanDevicesBroadcastReceiver(
    onDeviceFound: (BluetoothDevice) -> Unit,
    onStartDiscovery: () -> Unit
) {
    val listOfFilters = listOf(
        BluetoothDevice.ACTION_FOUND,
        BluetoothAdapter.ACTION_DISCOVERY_STARTED
    )
    val intentFilter = IntentFilter()
    listOfFilters.forEach {
        intentFilter.addAction(it)
    }
    SystemBroadcastReceiver(intentFilter = intentFilter, onSystemEvent = { intent ->
        val action: String? = intent?.action
        Timber.d("BT Screen: Action " + action + "received")
        if (action != null) {
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        else
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    try {
                        val deviceName = device!!.name
                        val deviceHardwareAddress = device.address // MAC address
                        Timber.d("BT Found device: $deviceName - $deviceHardwareAddress")
                        onDeviceFound(device)
                    } catch (e: SecurityException) {
                        Timber.e(e.message)
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Timber.d("BT Discovery started")
                    onStartDiscovery()
                }
            }
        }
    })
}
