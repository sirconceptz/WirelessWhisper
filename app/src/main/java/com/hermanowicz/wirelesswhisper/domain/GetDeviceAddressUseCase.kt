package com.hermanowicz.wirelesswhisper.domain

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetDeviceAddressUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : () -> String? {
    @SuppressLint("HardwareIds")
    override fun invoke(): String? {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bluetoothManager.adapter
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            btAdapter.address
        } else {
            null
        }
    }
}
