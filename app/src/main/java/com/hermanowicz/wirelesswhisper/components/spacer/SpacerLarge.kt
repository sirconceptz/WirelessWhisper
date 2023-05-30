package com.hermanowicz.wirelesswhisper.components.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.height(LocalSpacing.current.large))
}
