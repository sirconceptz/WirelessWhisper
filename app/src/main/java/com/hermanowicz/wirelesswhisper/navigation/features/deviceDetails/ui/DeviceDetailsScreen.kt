package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

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
fun DeviceDetailsScreen(
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {
    val macAddress = viewModel.macAddress

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Device details",
                textAlign = TextAlign.Center
            )
        }
        item { DividerCardInside() }
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = macAddress,
                textAlign = TextAlign.Center
            )
        }
    }
}