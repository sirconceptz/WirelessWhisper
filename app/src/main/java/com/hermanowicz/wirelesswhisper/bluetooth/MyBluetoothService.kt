package com.hermanowicz.wirelesswhisper.bluetooth

import android.annotation.SuppressLint
import android.app.Notification
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
import androidx.core.app.NotificationManagerCompat
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.EncryptedMessage
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.domain.DecryptMessageUseCase
import com.hermanowicz.wirelesswhisper.domain.EncryptMessageUseCase
import com.hermanowicz.wirelesswhisper.domain.GenerateEncryptionKeyUseCase
import com.hermanowicz.wirelesswhisper.domain.GetDeviceAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveAllPairedDevicesUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveDeviceForAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.SaveMessageLocallyUseCase
import com.hermanowicz.wirelesswhisper.domain.SavePairedDeviceUseCase
import com.hermanowicz.wirelesswhisper.domain.UpdateDeviceConnectionStatusUseCase
import com.hermanowicz.wirelesswhisper.domain.UpdateEncryptionKeyInDeviceUseCase
import com.hermanowicz.wirelesswhisper.utils.Constants.BT_SERVICE_CHANNEL_ID
import com.hermanowicz.wirelesswhisper.utils.NotificationBuilder
import com.hermanowicz.wirelesswhisper.utils.NotificationChannel.Companion.createNotificationChannel
import com.hermanowicz.wirelesswhisper.utils.Secrets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

private const val TAG = "MY_BLUETOOTH_MANAGER"

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
// ... (Add other message types here as needed.)

@AndroidEntryPoint
class MyBluetoothService() : Service() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val context: Context = this

    private lateinit var notificationManager: NotificationManager
    private var clientThread: ClientThread? = null
    private var serverThread: ServerThread? = null
    private var connectedThread: ConnectedThread? = null
    private var currentMacAddress: String? = null

    @Inject
    lateinit var saveMessageLocallyUseCase: SaveMessageLocallyUseCase

    @Inject
    lateinit var getDeviceAddressUseCase: GetDeviceAddressUseCase

    @Inject
    lateinit var observeAllPairedDevicesUseCase: ObserveAllPairedDevicesUseCase

    @Inject
    lateinit var savePairedDevicesUseCase: SavePairedDeviceUseCase

    @Inject
    lateinit var updateDeviceConnectionStatusUseCase: UpdateDeviceConnectionStatusUseCase

    @Inject
    lateinit var encryptMessageUseCase: EncryptMessageUseCase

    @Inject
    lateinit var decryptMessageUseCase: DecryptMessageUseCase

    @Inject
    lateinit var observeDeviceAddressUseCase: ObserveDeviceForAddressUseCase

    @Inject
    lateinit var updateEncryptionKeyInDeviceUseCase: UpdateEncryptionKeyInDeviceUseCase

    @Inject
    lateinit var generateEncryptionKeyUseCase: GenerateEncryptionKeyUseCase

    private val job by lazy { SupervisorJob() }
    private val scope by lazy { CoroutineScope(Dispatchers.IO + job) }

    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        @SuppressLint("MissingPermission")
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
                if (text.length < 30) {
                    scope.launch {
                        updateEncryptionKeyInDeviceUseCase(mmSocket.remoteDevice.address, text.toByteArray())
                    }
                } else {
                    scope.launch {
                        val device =
                            observeDeviceAddressUseCase(mmSocket.remoteDevice.address).first()
                        val encryptedMessage = EncryptedMessage(
                            message = text,
                            timestamp = System.currentTimeMillis(),
                            iv = device.encryptionKey,
                            senderAddress = device.macAddress
                        )
                        val decryptedMessage: Message? = decryptMessageUseCase(encryptedMessage)
                        if (decryptedMessage != null) {
                            saveMessageLocallyUseCase(decryptedMessage)
                        } else {
                            exchangeNewEncryptionKey(mmSocket.remoteDevice.address)
                        }
                    }
                }
                showNotification()
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        suspend fun write(content: String) {
            try {
                val device =
                    scope.async { observeDeviceAddressUseCase(mmSocket.remoteDevice.address).first() }
                val decryptedMessage = Message(
                    id = null,
                    message = content,
                    timestamp = System.currentTimeMillis(),
                    senderAddress = getDeviceAddressUseCase()
                )
                val encryptedMessage =
                    encryptMessageUseCase(decryptedMessage, device.await().encryptionKey)
                withContext(Dispatchers.IO) {
                    mmOutStream.write(encryptedMessage.message.toByteArray())
                }
                scope.launch {
                    val message = Message(
                        id = null,
                        senderAddress = getDeviceAddressUseCase(),
                        receiverAddress = mmSocket.remoteDevice.address,
                        timestamp = System.currentTimeMillis(),
                        readOut = true,
                        received = false,
                        message = decryptedMessage.message
                    )
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

        suspend fun sendKey(key: ByteArray) {
            try {
                withContext(Dispatchers.IO) {
                    mmOutStream.write(key)
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

    private suspend fun exchangeNewEncryptionKey(
        address: String
    ) {
        val generatedKey = generateEncryptionKeyUseCase()
        connectedThread?.sendKey(generatedKey)
        updateEncryptionKeyInDeviceUseCase(
            address,
            generatedKey
        )
    }

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        with(NotificationManagerCompat.from(context)) {
            notify(
                42,
                NotificationBuilder.buildNotification(context)
                    .build()
            )
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

        @SuppressLint("MissingPermission")
        override fun run() {
            val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothAdapter = bluetoothManager.adapter
            // Keep listening until exception occurs or a socket is returned.
            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                try {
                    socket.connect()
                } catch (e: SecurityException) {
                    Timber.e(e.message) // todo: handle exception
                }

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                setConnectedThread(socket)
                val device = Device(
                    macAddress = socket.remoteDevice.address,
                    name = socket.remoteDevice.name ?: context.getString(R.string.unnamed),
                    connected = true
                )
                saveDeviceIfNotSaved(device = device, checkKey = true)
                Timber.d("Bluetooth: Connected to " + socket.remoteDevice.name)
                handler.post {
                    Toast.makeText(
                        context,
                        "Connected to " + socket.remoteDevice.name,
                        Toast.LENGTH_LONG
                    ).show()
                }
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

        @SuppressLint("MissingPermission")
        override fun run() {
            val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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
                    Timber.d(getString(R.string.connected_to) + " " + it.remoteDevice.name)
                    setConnectedThread(it)
                    val device = Device(
                        macAddress = socket.remoteDevice.address,
                        name = socket.remoteDevice.name ?: context.getString(R.string.unnamed),
                        connected = true
                    )
                    saveDeviceIfNotSaved(device = device, checkKey = false)
                    handler.post {
                        Toast.makeText(
                            context,
                            getString(R.string.connected_to) + " " + it.remoteDevice.name,
                            Toast.LENGTH_LONG
                        ).show()
                    }
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

    private fun setConnectedThread(it: BluetoothSocket) {
        connectedThread = ConnectedThread(it)
        connectedThread!!.start()
        currentMacAddress = it.remoteDevice.address
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Bluetooth service activated")
        if (intent != null) {
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
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initService() {
        startForeground()
        startServer()
    }

    private fun startServer() {
        serverThread = ServerThread()
        serverThread!!.start()
        Timber.d("Start Bluetooth server")
    }

    private fun connectDevice(macAddress: String) {
        clientThread = ClientThread(macAddress)
        clientThread!!.start()
        Timber.d("Connection bluetooth started with $macAddress")
    }

    private fun disconnectDevice() {
        if (clientThread != null) {
            try {
                clientThread!!.cancel()
            } catch (_: IOException) {
            }
        }
        if (currentMacAddress != null) {
            scope.launch {
                updateDeviceConnectionStatusUseCase(currentMacAddress!!, false)
                currentMacAddress = null
            }
        }
        startServer()
    }

    private fun sendMessage(content: String) {
        if (connectedThread != null) {
            scope.launch {
                connectedThread!!.write(content)
            }
        }
    }

    private fun saveDeviceIfNotSaved(
        device: Device,
        checkKey: Boolean
    ) {
        var saved = false
        var validKey = false
        scope.launch {
            observeAllPairedDevicesUseCase().collect { devices ->
                devices.forEach { singleDevice ->
                    if (singleDevice.macAddress == device.macAddress) {
                        saved = true
                        if (singleDevice.encryptionKey.isNotEmpty()) {
                            validKey = true
                        }
                    }
                }
                if (!saved) {
                    savePairedDevicesUseCase(device)
                    if (!validKey && checkKey) {
                        exchangeNewEncryptionKey(device.macAddress)
                    }
                } else {
                    updateDeviceConnectionStatusUseCase(device.macAddress, true)
                }
            }
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_CONNECT = "ACTION_CONNECT"
        const val ACTION_DISCONNECT = "ACTION_DISCONNECT"
        const val ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE"
    }

    private fun startForeground() {
        val channelId =
            createNotificationChannel(context, BT_SERVICE_CHANNEL_ID, getString(R.string.bluetooth))

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }
}
