package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside

@Composable
fun PairedDevicesScreen(
    viewModel: PairedDevicesViewModel = hiltViewModel()
) {
    val pairedDevices = viewModel.pairedDevices

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Paired devices",
                textAlign = TextAlign.Center
            )
        }
        item {
            DividerCardInside()
        }
        item {
            pairedDevices.forEach { device ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = device.name + " - " + device.address,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}