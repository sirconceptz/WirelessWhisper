package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.components.chatBubble.ChatReceived
import com.hermanowicz.wirelesswhisper.components.chatBubble.ChatSend
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun SingleChatScreen(
    navigationIconClick: () -> Unit,
    viewModel: SingleChatViewModel = hiltViewModel(),
    bluetoothService: Intent
) {
    val uiState by viewModel.uiState.collectAsState()

    TopBarScaffold(
        topBarText = uiState.device.name,
        navigationIcon = {
            IconButton(onClick = navigationIconClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    ) {
        ChatView(
            messageList = uiState.messageList,
            bluetoothService = bluetoothService,
            currentMessage = uiState.currentMessage,
            onCurrentMessageChange = {
                viewModel.onCurrentMessageChange(it)
            },
            clearTextField = { viewModel.clearCurrentMessage() }
        )
    }
}

@Composable
fun ChatView(
    messageList: List<Message>,
    bluetoothService: Intent,
    currentMessage: String,
    onCurrentMessageChange: (String) -> Unit,
    clearTextField: () -> Unit
) {
    Column() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MessagesBox(messageList)
        }
        MessageBar(
            bluetoothService,
            currentMessage,
            onCurrentMessageChange,
            clearTextField = clearTextField
        )
    }
}

@Composable
fun MessagesBox(messageList: List<Message>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        messageList.forEach { message ->
            item {
                if (message.received) {
                    ChatReceived(text = message.message, timestamp = message.timestamp)
                } else {
                    ChatSend(text = message.message, timestamp = message.timestamp)
                }
            }
        }
    }
}

@Composable
fun MessageBar(
    bluetoothService: Intent,
    currentMessage: String,
    onCurrentMessageChange: (String) -> Unit,
    clearTextField: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(LocalSpacing.current.small)
        ) {
            TextField(value = currentMessage, onValueChange = { onCurrentMessageChange(it) })
        }
        IconButton(onClick = {
            Toast.makeText(context, currentMessage, Toast.LENGTH_LONG).show()
            sendMessage(context, currentMessage, bluetoothService, clearTextField = clearTextField)
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = null)
        }
    }
}

private fun sendMessage(
    context: Context,
    message: String,
    bluetoothService: Intent,
    clearTextField: () -> Unit
) {
    bluetoothService.putExtra(MyBluetoothService.ACTION_SEND_MESSAGE, message)
    bluetoothService.action = MyBluetoothService.ACTION_SEND_MESSAGE
    context.startService(bluetoothService)
    clearTextField()
}

@Preview
@Composable
private fun Preview() {
    Column {
        SingleChatScreen({}, bluetoothService = Intent())
    }
}
