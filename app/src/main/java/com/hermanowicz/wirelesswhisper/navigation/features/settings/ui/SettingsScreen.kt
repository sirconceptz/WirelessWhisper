package com.hermanowicz.wirelesswhisper.navigation.features.settings.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun SettingsScreen() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(LocalSpacing.current.medium)) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Settings",
                textAlign = TextAlign.Center
            )
        }
    }
}