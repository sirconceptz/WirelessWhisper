package com.hermanowicz.wirelesswhisper.components.chatBubble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import com.hermanowicz.wirelesswhisper.ui.theme.Purple20
import com.hermanowicz.wirelesswhisper.utils.DateFormatter

@Composable
fun ChatReceived(
    text: String,
    timestamp: Long,
    deleteMode: Boolean,
    onClickDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = LocalSpacing.current.small,
                top = LocalSpacing.current.tiny,
                bottom = LocalSpacing.current.tiny
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                    }
                )
            },
        horizontalArrangement = Arrangement.Start
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth()
                    .padding(start = if (deleteMode) 8.dp else 0.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                if (deleteMode) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { onClickDelete() }
                    )
                }
                Column(
                    modifier = Modifier
                        .background(
                            color = Purple20,
                            shape = TriangleLeftEdgeShape(10)
                        )
                        .width(8.dp)
                        .fillMaxHeight()
                ) {}
                Column(
                    modifier = Modifier
                        .background(
                            color = Purple20,
                            shape = RoundedCornerShape(4.dp, 4.dp, 4.dp, 0.dp)
                        )
                        .padding(
                            vertical = LocalSpacing.current.tiny,
                            horizontal = LocalSpacing.current.small
                        )
                ) {
                    Text(
                        text = text,
                        style = TextStyle.Default.copy(fontSize = 14.sp)
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpacing.current.small),
                text = DateFormatter.getFullDate(timestamp),
                style = TextStyle.Default.copy(fontSize = 11.sp),
                textAlign = TextAlign.Start
            )
        }
    }
}

class TriangleLeftEdgeShape(private val offset: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = size.width + 1, y = size.height - offset)
            lineTo(x = size.width + 1, y = size.height)
            lineTo(x = size.width + 1 - offset, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}
