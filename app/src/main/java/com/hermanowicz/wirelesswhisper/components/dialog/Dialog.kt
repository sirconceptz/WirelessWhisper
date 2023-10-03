package com.hermanowicz.wirelesswhisper.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.dropdown.DropdownDevice
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DialogPrimary(
    onPositiveLabel: String,
    onPositiveRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = LocalSpacing.current.medium,
                        horizontal = LocalSpacing.current.small
                    ),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {
                content()
                ButtonPrimary(
                    text = onPositiveLabel,
                    onClick = onPositiveRequest
                )
                ButtonPrimary(
                    text = stringResource(id = R.string.close),
                    onClick = onDismissRequest
                )
            }
        }
    }
}

@Composable
fun DialogPermissions(
    onPositiveLabel: String,
    onPositiveRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = LocalSpacing.current.medium,
                        horizontal = LocalSpacing.current.small
                    ),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {
                Text(text = stringResource(id = R.string.permission_is_needed_to_perform_the_action))
                ButtonPrimary(
                    text = onPositiveLabel,
                    onClick = onPositiveRequest
                )
                ButtonPrimary(
                    text = stringResource(id = R.string.close),
                    onClick = onDismissRequest
                )
            }
        }
    }
}

@Composable
fun DialogNewMessage(
    label: String,
    selectedValue: String,
    dropdownVisible: Boolean,
    deviceList: List<Device>,
    onSelectDevice: (Device) -> Unit,
    showDropdown: (Boolean) -> Unit,
    onPositiveRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = LocalSpacing.current.medium,
                        horizontal = LocalSpacing.current.small
                    ),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold
                )
                DropdownDevice(
                    value = selectedValue,
                    itemList = deviceList,
                    onClick = { showDropdown(true) },
                    onChange = onSelectDevice,
                    visibleDropdown = dropdownVisible,
                    onDismiss = { showDropdown(false) }
                )
                ButtonPrimary(
                    text = stringResource(id = R.string.confirm),
                    onClick = onPositiveRequest
                )
                ButtonPrimary(
                    text = stringResource(id = R.string.close),
                    onClick = onDismissRequest
                )
            }
        }
    }
}
