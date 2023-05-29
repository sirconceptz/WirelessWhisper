package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside

@Composable
fun PairedDevicesScreen(
    onClickPairedDevice: (String) -> Unit,
    viewModel: PairedDevicesViewModel = hiltViewModel()
) {
    val pairedDevices = viewModel.pairedDevices

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.paired_devices),
                textAlign = TextAlign.Center
            )
        }
        item {
            DividerCardInside()
        }
        item {
            pairedDevices.forEach { device ->
                CardPrimary {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickPairedDevice(device.address) },
                        text = device.name,
                        style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.surface, textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }
}