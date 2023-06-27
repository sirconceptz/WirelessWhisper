package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
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
    bottomBar: @Composable () -> Unit,
    viewModel: SingleChatViewModel = hiltViewModel(),
    bluetoothService: Intent
) {
    val uiState by viewModel.uiState.collectAsState()

    TopBarScaffold(
        //topBarText = uiState.device.name,
        topBarText = "Single chat",
        //bottomBar = bottomBar
    ) {
        ChatView(uiState.messageList, bluetoothService)
    }
}

@Composable
fun ChatView(messageList: List<Message>, bluetoothService: Intent) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            MessagesBox(messageList)
        }
        MessageBar(bluetoothService)
    }
}

@Composable
fun MessagesBox(messageList: List<Message>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        messageList.forEach { message ->
            item {
                if (message.received) {
                    ChatReceived(text = message.message)
                } else {
                    ChatSend(text = message.message)
                }
            }
        }
    }
}

@Composable
fun MessageBar(bluetoothService: Intent) {
    val context = LocalContext.current
    var rememberText = remember { TextFieldValue("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(50.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(LocalSpacing.current.small)
        ) {
            TextField(
                value = rememberText,
                onValueChange = { rememberText = it }
            )
        }
        IconButton(onClick = {
            Toast.makeText(context, rememberText.text, Toast.LENGTH_LONG).show()
            sendMessage(context, rememberText.text, bluetoothService)
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = null)
        }
    }
}

private fun sendMessage(context: Context, message: String, bluetoothService: Intent) {
    bluetoothService.putExtra(MyBluetoothService.ACTION_SEND_MESSAGE, message)
    bluetoothService.action = MyBluetoothService.ACTION_SEND_MESSAGE
    context.startService(bluetoothService)
}

@Preview
@Composable
private fun Preview() {
    Column {
        SingleChatScreen({}, bluetoothService = Intent())
    }
}
