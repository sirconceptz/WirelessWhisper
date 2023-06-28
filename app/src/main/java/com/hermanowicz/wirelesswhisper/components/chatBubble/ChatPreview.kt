package com.hermanowicz.wirelesswhisper.components.chatBubble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Preview
@Composable
private fun Preview() {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.tiny)
        ) {
            ChatReceived("Short message", System.currentTimeMillis())
            ChatSend("My short message", System.currentTimeMillis())
            ChatReceived("But this is long message and I have to test how it's working with longer texts.", System.currentTimeMillis())
            ChatSend("My second short message", System.currentTimeMillis())
            ChatSend("My third short message", System.currentTimeMillis())
        }
    }
}
