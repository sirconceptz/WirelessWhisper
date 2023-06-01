package com.hermanowicz.wirelesswhisper.receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BluetoothBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        val state: Int
        val bluetoothDevice: BluetoothDevice?

        Timber.d("BroadcastActions", "Action " + action + "received")

        when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show()
                        Timber.d("BroadcastActions", "Bluetooth is off")
                    }

                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Toast.makeText(context, "Bluetooth is turning off", Toast.LENGTH_SHORT)
                            .show()
                        Timber.d("BroadcastActions", "Bluetooth is turning off")
                    }

                    BluetoothAdapter.STATE_ON -> {
                        Timber.d("BroadcastActions", "Bluetooth is on")
                    }
                }
            }

            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                try {
                    Toast.makeText(
                        context,
                        "Connected to " + bluetoothDevice!!.name,
                        Toast.LENGTH_SHORT
                    ).show()
                    Timber.d("BroadcastActions", "Connected to " + bluetoothDevice.name)
                } catch (e: SecurityException) {
                    Timber.e("BroadcastActions", "Error: $e")
                } catch (e: Exception) {
                    Timber.e("BroadcastActions", "Error: $e")
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                try {
                    Toast.makeText(
                        context,
                        "Disconnected from " + bluetoothDevice!!.name,
                        Toast.LENGTH_SHORT
                    ).show()
                    Timber.d("BroadcastActions", "Connected to " + bluetoothDevice.name)
                } catch (e: SecurityException) {
                    Timber.e("BroadcastActions", "Error: $e")
                } catch (e: Exception) {
                    Timber.e("BroadcastActions", "Error: $e")
                }
            }
            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                try {
                    Toast.makeText(
                        context,
                        "Disconnected from " + bluetoothDevice!!.name,
                        Toast.LENGTH_SHORT
                    ).show()
                    Timber.d("BroadcastActions", "Connected to " + bluetoothDevice.name)
                } catch (e: SecurityException) {
                    Timber.e("BroadcastActions", "Error: $e")
                } catch (e: Exception) {
                    Timber.e("BroadcastActions", "Error: $e")
                }
            }
        }
    }
}
