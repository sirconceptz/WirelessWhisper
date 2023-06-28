package com.hermanowicz.wirelesswhisper.bluetooth

import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.domain.GetDeviceAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.SaveMessageLocallyUseCase
import com.hermanowicz.wirelesswhisper.utils.Secrets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

private const val TAG = "MY_BLUETOOTH_MANAGER"

const val NOTIFY_ID = 5
// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
// ... (Add other message types here as needed.)

@AndroidEntryPoint
class MyBluetoothService() : Service() {

    private val handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var notificationManager: NotificationManager
    private lateinit var clientThread: ClientThread
    private lateinit var serverThread: ServerThread
    private var connectedThread: ConnectedThread? = null

    @Inject
    lateinit var saveMessageLocallyUseCase: SaveMessageLocallyUseCase

    @Inject
    lateinit var getDeviceAddressUseCase: GetDeviceAddressUseCase

    private val job by lazy { SupervisorJob() }
    private val scope by lazy { CoroutineScope(Dispatchers.IO + job) }

    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Timber.d(TAG, "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ,
                    numBytes,
                    -1,
                    mmBuffer
                )
                val text = String(mmBuffer, 0, readMsg.arg1)

                scope.launch {
                    val message = Message(
                        id = null,
                        senderAddress = mmSocket.remoteDevice.address,
                        receiverAddress = getDeviceAddressUseCase(),
                        timestamp = System.currentTimeMillis(),
                        received = true,
                        message = text
                    )
                    Timber.d("Received message: $message")
                    saveMessageLocallyUseCase(message)
                }
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(content: String) {
            try {
                mmOutStream.write(content.toByteArray())
                scope.launch {
                    val message = Message(
                        id = null,
                        senderAddress = getDeviceAddressUseCase(),
                        receiverAddress = mmSocket.remoteDevice.address,
                        timestamp = System.currentTimeMillis(),
                        received = false,
                        message = content
                    )
                    Timber.d("Sent: $message")
                    saveMessageLocallyUseCase(message)
                }
            } catch (e: IOException) {
                Timber.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE,
                -1,
                -1,
                mmBuffer
            )
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
                Timber.d("Bluetooth server closed")
            } catch (e: IOException) {
                Timber.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    inner class ClientThread(address: String) : Thread() {

        private lateinit var bluetoothAdapter: BluetoothAdapter

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            val uuid = UUID.fromString(Secrets.MY_UUID)
            try {
                val device = bluetoothAdapter.getRemoteDevice(address)
                device.createRfcommSocketToServiceRecord(uuid)
            } catch (e: SecurityException) {
                Timber.e(e.message)
                null
            }
        }

        override fun run() {
            val bluetoothManager =
                getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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
                connectedThread = ConnectedThread(mmSocket!!)
                connectedThread!!.start()
                Timber.d("Bluetooth: Connected to " + socket.remoteDevice.address)
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

    inner class ServerThread : Thread() {

        private lateinit var bluetoothAdapter: BluetoothAdapter

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            val uuid = UUID.fromString(Secrets.MY_UUID)
            try {
                bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(Secrets.APP_NAME, uuid)
            } catch (e: SecurityException) {
                null
            }
        }

        override fun run() {
            val bluetoothManager =
                getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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
                    Timber.d("Bluetooth: Connected to " + it.remoteDevice.address)
                    connectedThread = ConnectedThread(it)
                    connectedThread!!.start()
                    handler.post {
                        Toast.makeText(
                            this@MyBluetoothService,
                            "Connected to " + socket.remoteDevice.address,
                            Toast.LENGTH_LONG
                        ).show()
                    }
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

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Timber.d("Bluetooth service activated")
        when (intent.action) {
            ACTION_START -> initService()
            ACTION_CONNECT -> {
                val macAddress = intent.getStringExtra(ACTION_CONNECT)
                if (macAddress != null) {
                    connectDevice(macAddress)
                }
            }

            ACTION_DISCONNECT -> disconnectDevice()
            ACTION_SEND_MESSAGE -> {
                val message = intent.getStringExtra(ACTION_SEND_MESSAGE)
                if (message != null) {
                    sendMessage(message)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initService() {
        //startForeground(NOTIFY_ID, generateNotification().build())
        startServer()
    }

    private fun startServer() {
        serverThread = ServerThread()
        serverThread.start()
        Timber.d("Start Bluetooth server")
    }

    private fun connectDevice(macAddress: String) {
        clientThread = ClientThread(macAddress)
        clientThread.start()
        Timber.d("Connection bluetooth started with $macAddress")
    }

    private fun disconnectDevice() {
        clientThread.cancel()
    }

    private fun sendMessage(message: String) {
        if(connectedThread != null) {
            connectedThread!!.write(message)
        }
    }

    private fun generateNotification(): NotificationCompat.Builder {
        val mainNotificationText = getString(R.string.app_name)
        val titleText = getString(R.string.chat_is_active)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(getString(R.string.chat_is_active))
            .setBigContentTitle(getString(R.string.app_name))

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, "BT ACTIVE")

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_CONNECT = "ACTION_CONNECT"
        const val ACTION_DISCONNECT = "ACTION_DISCONNECT"
        const val ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE"
    }
}
