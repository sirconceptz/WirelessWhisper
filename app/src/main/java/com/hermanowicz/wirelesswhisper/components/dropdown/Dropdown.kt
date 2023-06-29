package com.hermanowicz.wirelesswhisper.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
            modifier = Modifier.padding(LocalSpacing.current.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value, color = MaterialTheme.colors.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = value,
                    color = MaterialTheme.colors.onSurface
                )
                DropdownMenu(expanded = visibleDropdown, onDismissRequest = { onDismiss() }) {
                    itemList.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onChange(item)
                            onDismiss()
                        }) {
                            Text(text = item.name, color = MaterialTheme.colors.onSurface)
                        }
                    }
                }
            }
        }
    }
}