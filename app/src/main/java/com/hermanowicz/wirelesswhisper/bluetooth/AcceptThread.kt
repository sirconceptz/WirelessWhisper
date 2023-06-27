package com.hermanowicz.wirelesswhisper.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.hermanowicz.wirelesswhisper.utils.Secrets.APP_NAME
import com.hermanowicz.wirelesswhisper.utils.Secrets.MY_UUID
import timber.log.Timber
import java.io.IOException
import java.util.UUID

private const val TAG = "BT AcceptThread"

class AcceptThread(private val context: Context) : Thread() {

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        val uuid = UUID.fromString(MY_UUID)
        try {
            bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, uuid)
        } catch (e: SecurityException) {
            null
        }
    }

    override fun run() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Timber.e(TAG, "Socket's accept() method failed", e)
                shouldLoop = false
                null
            }
            socket?.also {
                Timber.d("Connected to " + it.remoteDevice.address)
                // manageMyConnectedSocket(it)
                val myBluetoothService = MyBluetoothService()
                val connectedThread = myBluetoothService.ConnectedThread(it)
                connectedThread.start()
                mmServerSocket?.close()
                shouldLoop = false
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    fun cancel() {
        try {
            mmServerSocket?.close()
        } catch (e: IOException) {
            Timber.e(TAG, "Could not close the connect socket", e)
        }
    }
}
