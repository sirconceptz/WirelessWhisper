package com.hermanowicz.wirelesswhisper.components.switches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@Composable
fun SwitchPrimary(
    modifier: Modifier = Modifier,
    label: String,
    state: Boolean,
    onStateChange: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LocalSpacing.current.small))
            .padding(LocalSpacing.current.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )
                )
                Text(
                    text = if (state) stringResource(id = R.string.enabled) else stringResource(id = R.string.disabled),
                    style = TextStyle(fontSize = 14.sp),
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            Switch(checked = state, onCheckedChange = { onStateChange() }, enabled = enabled)
        }
    }
}
