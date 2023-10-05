package com.hermanowicz.wirelesswhisper.domain

import android.bluetooth.BluetoothManager
import android.content.Context
import android.widget.Toast
import com.hermanowicz.wirelesswhisper.R
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class StartScanningDevicesUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : () -> Unit {
    override fun invoke() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bluetoothManager.adapter
        try {
            btAdapter.startDiscovery()
        } catch (e: SecurityException) {
            Timber.e(e.message)
            Toast.makeText(context, context.getString(R.string.error_bluetooth_is_not_active), Toast.LENGTH_LONG).show()
        }
    }
}
