package com.hermanowicz.wirelesswhisper.receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hermanowicz.wirelesswhisper.domain.UpdateDevicesConnectionStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothEventsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateDevicesConnectionStatusUseCase: UpdateDevicesConnectionStatusUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action
        if (action != null) {
            if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                Timber.d("action state changed")

                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_DISCONNECTING -> {
                        Timber.d("BluetoothAdapter.STATE_DISCONNECTING")
                    }
                }
            }
        }
    }
}
