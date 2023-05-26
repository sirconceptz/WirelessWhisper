package com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MainScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text("Main Screen", textAlign = TextAlign.Center)
        }
    }
}
