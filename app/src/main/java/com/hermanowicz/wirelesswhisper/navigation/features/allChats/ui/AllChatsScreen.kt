package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold

@Composable
fun AllChatsScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: AllChatsViewModel = hiltViewModel()
) {
    TopBarScaffold(
        topBarText = stringResource(id = R.string.all_chats),
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
            }
        }
    }
}
