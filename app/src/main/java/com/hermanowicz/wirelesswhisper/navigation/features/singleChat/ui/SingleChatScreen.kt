package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.components.chatBubble.ChatError
import com.hermanowicz.wirelesswhisper.components.chatBubble.ChatReceived
import com.hermanowicz.wirelesswhisper.components.chatBubble.ChatSend
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPermissions
import com.hermanowicz.wirelesswhisper.components.permissions.permissionChecker
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.domain.GoToPermissionSettingsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.state.SingleChatUiState
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import com.hermanowicz.wirelesswhisper.utils.Permissions

@Composable
fun SingleChatScreen(
    navigationIconClick: () -> Unit,
    viewModel: SingleChatViewModel = hiltViewModel(),
    bluetoothService: Intent
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    val launcherPermissionsSendMessage = permissionChecker(
        onGranted = {
            sendMessageIfConnected(viewModel, context, uiState, bluetoothService)
        },
        showDialogPermissionNeeded = { viewModel.onGoToPermissionSettings(true) }
    )

    Dialogs(uiState, viewModel, launcherPermissionsSendMessage)

    LaunchedEffect(key1 = uiState.goToPermissionSettings) {
        if (uiState.goToPermissionSettings) {
            GoToPermissionSettingsUseCase.invoke(context)
            viewModel.onGoToPermissionSettings(false)
        }
    }

    TopBarScaffold(topBarText = uiState.device?.name ?: "", navigationIcon = {
        IconButton(onClick = navigationIconClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }, actions = {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(end = LocalSpacing.current.small)
                .align(Alignment.CenterVertically)
                .clickable { viewModel.onClickEditMode(!uiState.deleteMode) }
        )
    }) {
        ChatView(
            messageList = uiState.messageList,
            currentMessage = uiState.currentMessage,
            checkPermissions = {
                if (viewModel.isNeededPermissionsGranted())
                    sendMessageIfConnected(viewModel, context, uiState, bluetoothService)
                else
                    viewModel.showDialogPermissionsSendMessage(true)
            },
            onCurrentMessageChange = {
                viewModel.onCurrentMessageChange(it)
            },
            deleteMode = uiState.deleteMode,
            setCurrentMessageInState = { viewModel.onCurrentMessageChange(it) },
            onClickDelete = { viewModel.deleteSingleMessage(it.id!!) },
            onCopyMessageToClipboard = { viewModel.copyMessageToClipboard(it) }
        )
    }
}

private fun sendMessageIfConnected(
    viewModel: SingleChatViewModel,
    context: Context,
    uiState: SingleChatUiState,
    bluetoothService: Intent
) {
    if (viewModel.isDeviceConnected()) {
        sendMessage(
            context,
            uiState.currentMessage,
            bluetoothService,
            clearTextField = { viewModel.clearCurrentMessage() }
        )
    } else {
        showErrorDeviceNotConnected(context)
    }
}

fun showErrorDeviceNotConnected(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.error_device_is_not_connected),
        Toast.LENGTH_LONG
    ).show()
}

@Composable
private fun Dialogs(
    uiState: SingleChatUiState,
    viewModel: SingleChatViewModel,
    launcherPermissionsSendMessage: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
) {
    if (uiState.showDialogPermissionsSendMessage) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.showDialogPermissionsSendMessage(false)
                launcherPermissionsSendMessage.launch(Permissions.btPermissions)
            },
            onDismissRequest = { viewModel.showDialogPermissionsSendMessage(false) }
        )
    }
}

@Composable
fun ChatView(
    messageList: List<Message>,
    currentMessage: String,
    checkPermissions: () -> Unit,
    onCurrentMessageChange: (String) -> Unit,
    deleteMode: Boolean,
    setCurrentMessageInState: (String) -> Unit,
    onClickDelete: (Message) -> Unit,
    onCopyMessageToClipboard: (String) -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MessagesBox(
                messageList,
                deleteMode = deleteMode,
                onClickDelete = onClickDelete,
                onCopyMessageToClipboard = onCopyMessageToClipboard
            )
        }
        MessageBar(
            currentMessage,
            checkPermissions,
            onCurrentMessageChange,
            setCurrentMessageInState
        )
    }
}

@Composable
fun MessagesBox(
    messageList: List<Message>,
    deleteMode: Boolean,
    onClickDelete: (Message) -> Unit,
    onCopyMessageToClipboard: (String) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyColumnListState
    ) {
        messageList.forEach { message ->
            item {
                if (message.received && !message.error) {
                    ChatReceived(
                        text = message.message,
                        timestamp = message.timestamp,
                        deleteMode = deleteMode,
                        onClickDelete = { onClickDelete(message) },
                        onCopyMessageToClipboard = { onCopyMessageToClipboard(it) }
                    )
                } else if (!message.received && !message.error) {
                    ChatSend(
                        text = message.message,
                        timestamp = message.timestamp,
                        deleteMode = deleteMode,
                        onClickDelete = { onClickDelete(message) },
                        onCopyMessageToClipboard = { onCopyMessageToClipboard(it) }
                    )
                } else {
                    ChatError(
                        timestamp = message.timestamp,
                        deleteMode = deleteMode,
                        onClickDelete = { onClickDelete(message) },
                        onCopyMessageToClipboard = { onCopyMessageToClipboard(it) }
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
    currentMessage: String,
    checkPermissions: () -> Unit,
    onCurrentMessageChange: (String) -> Unit,
    setCurrentMessageInState: (String) -> Unit
) {
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
            setCurrentMessageInState(currentMessage)
            checkPermissions()
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
    context.startForegroundService(bluetoothService)
    clearTextField()
}

@Preview
@Composable
private fun Preview() {
    Column {
        SingleChatScreen({}, bluetoothService = Intent())
    }
}
