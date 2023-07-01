package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun AllChatsScreen(
    onClickSingleChat: (String) -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: AllChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showDialogNewMessage) {
        DialogNewMessage(
            label = stringResource(id = R.string.new_message),
            selectedValue = uiState.newMessageDevice?.name ?: "",
            deviceList = uiState.connectedDevices,
            dropdownVisible = uiState.showDropdownNewMessage,
            showDropdown = { viewModel.showNewMessageDropdown(it) },
            onSelectDevice = { viewModel.onSelectNewMessageDevice(it) },
            onPositiveRequest = { onClickSingleChat(uiState.newMessageDevice!!.macAddress) },
            onDismissRequest = { viewModel.showDialogNewMessage(false) }
        )
    }

    TopBarScaffold(
        topBarText = stringResource(id = R.string.all_chats),
        bottomBar = bottomBar,
        actions = {
            Text(
                modifier = Modifier.clickable {
                    viewModel.showDialogNewMessage(true)
                },
                text = stringResource(id = R.string.new_),
                color = Color.White
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSpacing.current.medium)
        ) {
            uiState.chatList.forEach {
                item {
                    Box(modifier = Modifier.clickable { onClickSingleChat(it.macAddress) }) {
                        CardPrimary {
                            if (it.unreadMessages == 0) {
                                Text(text = it.deviceName, color = Color.White)
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = it.deviceName, color = Color.White)
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
                                            text = it.unreadMessages.toString(),
                                            color = Color.Black,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
