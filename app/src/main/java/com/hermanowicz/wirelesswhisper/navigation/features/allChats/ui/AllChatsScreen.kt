package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.dialog.DialogNewMessage
import com.hermanowicz.wirelesswhisper.components.spacer.SpacerLarge
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.data.model.Chat
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.state.AllChatsUiState
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllChatsScreen(
    onClickSingleChat: (Device?) -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: AllChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    Dialogs(uiState, viewModel, onClickSingleChat)

    TopBarScaffold(
        topBarText = stringResource(id = R.string.all_chats),
        bottomBar = bottomBar,
        actions = {
            Button(
                onClick = {
                    viewModel.showDialogNewMessage(true)
                }
            ) {
                Text(text = stringResource(id = R.string.new_))
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.medium)
        ) {
            itemsIndexed(
                items = uiState.chatList,
                key = { _: Int, item: Chat -> item.hashCode() }
            ) { _: Int, item: Chat ->
                val state = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            viewModel.deleteSingleChat(item.device.macAddress)
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    state = state,
                    background = {
                        val color = when (state.dismissDirection) {
                            DismissDirection.StartToEnd -> Color.Transparent
                            DismissDirection.EndToStart -> Color.Red
                            null -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    },
                    dismissContent = {
                        SingleChat(onClickSingleChat = onClickSingleChat, chat = item)
                    },
                    directions = setOf(DismissDirection.EndToStart)
                )
            }
        }
    }
}

@Composable
private fun Dialogs(
    uiState: AllChatsUiState,
    viewModel: AllChatsViewModel,
    onClickSingleChat: (Device?) -> Unit
) {
    if (uiState.showDialogNewMessage) {
        DialogNewMessage(
            label = stringResource(id = R.string.new_message),
            selectedValue = uiState.newMessageDevice?.name ?: "",
            deviceList = uiState.connectedDevices,
            dropdownVisible = uiState.showDropdownNewMessage,
            showDropdown = { viewModel.showNewMessageDropdown(it) },
            onSelectDevice = { viewModel.onSelectNewMessageDevice(it) },
            onPositiveRequest = { onClickSingleChat(uiState.newMessageDevice) },
            onDismissRequest = { viewModel.showDialogNewMessage(false) }
        )
    }
}

@Composable
private fun SingleChat(
    onClickSingleChat: (Device?) -> Unit,
    chat: Chat
) {
    Box(modifier = Modifier.clickable { onClickSingleChat(chat.device) }) {
        CardPrimary {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (chat.unreadMessages == 0) {
                    Text(text = chat.device.name, color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    ConnectionStatusBox(status = chat.deviceConnectionStatus)
                } else {
                    Text(text = chat.device.name, color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    UnreadMessagesBox(chat.unreadMessages.toString())
                    SpacerLarge()
                    ConnectionStatusBox(status = chat.deviceConnectionStatus)
                }
            }
        }
    }
}

@Composable
private fun UnreadMessagesBox(counter: String) {
    Box(
        modifier = Modifier
            .height(30.dp)
            .aspectRatio(1f)
            .background(Color.White, shape = CircleShape)
            .border(
                BorderStroke(3.dp, Color.Black),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = counter,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ConnectionStatusBox(status: Boolean) {
    Box(
        modifier = Modifier
            .height(20.dp)
            .aspectRatio(1f)
            .background(if (status) Color.Green else Color.Red, shape = CircleShape)
            .border(
                BorderStroke(0.dp, Color.Black),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {}
}
