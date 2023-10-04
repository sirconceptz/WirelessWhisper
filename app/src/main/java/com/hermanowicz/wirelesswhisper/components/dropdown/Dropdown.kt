package com.hermanowicz.wirelesswhisper.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun DropdownDevice(
    value: String,
    itemList: List<Device>,
    onClick: () -> Unit,
    onChange: (Device) -> Unit,
    visibleDropdown: Boolean,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Column(
            modifier = Modifier.padding(LocalSpacing.current.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value, color = MaterialTheme.colorScheme.onSurface)
                DropdownMenu(expanded = visibleDropdown, onDismissRequest = { onDismiss() }) {
                    itemList.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                onChange(item)
                                onDismiss()
                            },
                            text = {
                                Text(text = item.name, color = MaterialTheme.colorScheme.onSurface)
                            }
                        )
                    }
                }
            }
        }
    }
}
