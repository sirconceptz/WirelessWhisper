package com.hermanowicz.wirelesswhisper.bluetooth

import android.content.Context
import android.content.Intent

class BluetoothAction {
    companion object {
        fun goToAction(context: Context, action: String) {
            val bluetoothServiceIntent = Intent(context, MyBluetoothService::class.java)
            bluetoothServiceIntent.action = action
            context.startForegroundService(bluetoothServiceIntent)
        }
    }
}
