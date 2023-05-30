package com.hermanowicz.wirelesswhisper.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun CardPrimary(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = LocalSpacing.current.small
            )
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(LocalSpacing.current.medium)
        ) {
            content()
        }
    }
}
