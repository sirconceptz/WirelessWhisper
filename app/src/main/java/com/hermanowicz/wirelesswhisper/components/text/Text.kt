package com.hermanowicz.wirelesswhisper.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun TextLabel(
    text: String
) {
    Text(
        text = text,
        style = TextStyle(fontSize = 20.sp)
    )
}