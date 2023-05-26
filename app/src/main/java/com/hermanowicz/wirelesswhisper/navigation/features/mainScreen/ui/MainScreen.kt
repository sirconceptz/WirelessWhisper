package com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun MainScreen(
    onNavigateToPairedDevices: () -> Unit,
    onNavigateToScanDevices: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalSpacing.current.medium)
    ) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToPairedDevices() },
                content = { Text(text = stringResource(id = R.string.paired_devices)) }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToScanDevices() },
                content = { Text(text = stringResource(id = R.string.scan_devices)) }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToSettings() },
                content = { Text(text = stringResource(id = R.string.settings)) }
            )
        }
    }
}
