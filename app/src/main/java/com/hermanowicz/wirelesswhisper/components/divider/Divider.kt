package com.hermanowicz.wirelesswhisper.components.divider

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DividerCardInside() {
    Divider(
        color = MaterialTheme.colors.onSurface,
        thickness = LocalSpacing.current.line
    )
}
