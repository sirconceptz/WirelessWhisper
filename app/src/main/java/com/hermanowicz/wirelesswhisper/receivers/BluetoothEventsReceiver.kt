package com.hermanowicz.wirelesswhisper.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BluetoothEventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action: String? = intent?.action
        if (action != null) {
            if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                Timber.d("Bluetooth: Device disconnected - ACL DISCONNECTED")
                val bluetoothService = Intent(context, MyBluetoothService::class.java)
                bluetoothService.action = MyBluetoothService.ACTION_UPDATE_DEVICE_CONNECTION_STATUS
                context.startService(bluetoothService)
            }
        }
    }
}
