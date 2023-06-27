package com.hermanowicz.wirelesswhisper.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.hermanowicz.wirelesswhisper.utils.Secrets
import timber.log.Timber
import java.io.IOException
import java.util.UUID

class ConnectThread(private val context: Context, address: String) : Thread() {

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        val uuid = UUID.fromString(Secrets.MY_UUID)
        try {
            val device = bluetoothAdapter.getRemoteDevice(address)
            device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: SecurityException) {
            null
        }
    }

    override fun run() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        // Keep listening until exception occurs or a socket is returned.
        mmSocket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            try {
                socket.connect()
            } catch (e: SecurityException) {
                Timber.e(e.message)
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            Timber.d("Connected to " + socket.remoteDevice.address)
            // val handler = Handler(Looper.getMainLooper())
            val myBluetoothService = MyBluetoothService()
            val connectedThread = myBluetoothService.ConnectedThread(mmSocket!!)
            connectedThread.write("TEST blutacza".toByteArray())
            connectedThread.write("TEST blutacza2".toByteArray())
            connectedThread.write("TEST blutacza3".toByteArray())
        }
    }

    // Closes the connect socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) {
            Timber.e("Could not close the connect socket", e)
        }
    }
}
