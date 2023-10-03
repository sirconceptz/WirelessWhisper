package com.hermanowicz.wirelesswhisper.bluetooth

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
            if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                Timber.d("Bluetooth: Device disconnected - ACL DISCONNECTED")
                BluetoothAction.goToAction(
                    context,
                    MyBluetoothService.ACTION_UPDATE_DEVICE_CONNECTION_STATUS
                )
            }
        }
    }
}
