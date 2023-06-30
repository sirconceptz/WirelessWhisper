package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
                    Box(modifier = Modifier.clickable { onClickSingleChat(it.second) }) {
                        CardPrimary {
                            Text(text = it.first, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
