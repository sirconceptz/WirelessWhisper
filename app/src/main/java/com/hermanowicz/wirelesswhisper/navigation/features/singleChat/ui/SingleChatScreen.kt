package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { viewModel.onClickEditMode(!uiState.deleteMode) }
            )
        }
    ) {
        ChatView(
            messageList = uiState.messageList,
            bluetoothService = bluetoothService,
            currentMessage = uiState.currentMessage,
            onCurrentMessageChange = {
                viewModel.onCurrentMessageChange(it)
            },
            clearTextField = { viewModel.clearCurrentMessage() },
            deleteMode = uiState.deleteMode,
            onClickDelete = { viewModel.deleteSingleMessage(it.id!!) }
        )
    }
}

@Composable
fun ChatView(
    messageList: List<Message>,
    bluetoothService: Intent,
    currentMessage: String,
    onCurrentMessageChange: (String) -> Unit,
    clearTextField: () -> Unit,
    deleteMode: Boolean,
    onClickDelete: (Message) -> Unit
) {
    Column() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MessagesBox(messageList, deleteMode = deleteMode, onClickDelete = onClickDelete)
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
fun MessagesBox(messageList: List<Message>, deleteMode: Boolean, onClickDelete: (Message) -> Unit) {
    val lazyColumnListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyColumnListState
    ) {
        messageList.forEach { message ->
            item {
                if (message.received) {
                    ChatReceived(
                        text = message.message,
                        timestamp = message.timestamp,
                        deleteMode = deleteMode,
                        onClickDelete = { onClickDelete(message) }
                    )
                } else {
                    ChatSend(
                        text = message.message,
                        timestamp = message.timestamp,
                        deleteMode = deleteMode,
                        onClickDelete = { onClickDelete(message) }
                    )
                }
            }
        }
    }

    LaunchedEffect(messageList.size) {
        if (messageList.size > 1) {
            lazyColumnListState.scrollToItem(messageList.size - 1)
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
