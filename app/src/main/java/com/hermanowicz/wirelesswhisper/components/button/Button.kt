package com.hermanowicz.wirelesswhisper.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(vertical = LocalSpacing.current.tiny),
            onClick = onClick
        ) {
            Text(text = text, color = Color.White)
        }
    }
}
