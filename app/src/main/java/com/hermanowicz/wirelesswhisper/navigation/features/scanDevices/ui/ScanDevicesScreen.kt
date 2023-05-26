package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun ScanDevicesScreen(
    viewModel: ScanDevicesViewModel = hiltViewModel()
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Scan devices",
                textAlign = TextAlign.Center
            )
        }
    }
}