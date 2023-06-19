package com.hermanowicz.wirelesswhisper.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun CardWhiteBgWithBorder(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        border = BorderStroke(width = LocalSpacing.current.line, color = Color.Black),
        shape = RoundedCornerShape(LocalSpacing.current.small)
    ) {
        Column {
            content()
        }
    }
}