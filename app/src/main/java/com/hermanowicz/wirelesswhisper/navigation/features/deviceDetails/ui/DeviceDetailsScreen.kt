package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffold
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DeviceDetailsScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {
    val macAddress = viewModel.macAddress

    TopBarScaffold(
        topBarText = stringResource(id = R.string.device_details),
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.medium)
        ) {
            item {
                CardPrimary {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.name),
                            color = Color.White
                        )
                        Text(
                            text = "to do observe",
                            color = Color.White
                        )
                    }
                    DividerCardInside(
                        modifier = Modifier.padding(vertical = LocalSpacing.current.small),
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.mac_address),
                            color = Color.White
                        )
                        Text(
                            text = macAddress,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
