package com.hermanowicz.wirelesswhisper.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BluetoothEventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action: String? = intent?.action
        if (action != null) {
            when (action) {
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    updateDevicesConnectionStatus(context)
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    if (state == BluetoothAdapter.STATE_OFF) {
                        stopServer(context)
                    } else {
                        startServer(context)
                    }
                }
            }
        }
    }

    private fun startServer(context: Context) {
        Timber.d("Bluetooth: Turned on - start server")
        BluetoothAction.goToAction(
            context, MyBluetoothService.ACTION_START
        )
    }

    private fun stopServer(context: Context) {
        Timber.d("Bluetooth: Turned off - stop server")
        BluetoothAction.goToAction(
            context, MyBluetoothService.ACTION_STOP
        )
    }

    private fun updateDevicesConnectionStatus(context: Context) {
        Timber.d("Bluetooth: Device disconnected - ACL DISCONNECTED")
        BluetoothAction.goToAction(
            context, MyBluetoothService.ACTION_UPDATE_DEVICE_CONNECTION_STATUS
        )
    }
}
