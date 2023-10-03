package com.hermanowicz.wirelesswhisper.components.divider

import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DividerCardInside(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = LocalSpacing.current.line
    )
}
