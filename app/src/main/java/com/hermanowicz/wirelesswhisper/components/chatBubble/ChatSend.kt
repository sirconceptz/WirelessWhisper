package com.hermanowicz.wirelesswhisper.components.chatBubble

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import com.hermanowicz.wirelesswhisper.ui.theme.Purple80

@Composable
fun ChatSend(
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = LocalSpacing.current.small,
                top = LocalSpacing.current.tiny,
                bottom = LocalSpacing.current.tiny
            ),
        horizontalArrangement = Arrangement.End
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(
                modifier = Modifier
                    .background(
                        color = Purple80,
                        shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                    )
                    .padding(
                        vertical = LocalSpacing.current.tiny,
                        horizontal = LocalSpacing.current.small
                    )
            ) {
                Text(text = text)
            }
            Column(
                modifier = Modifier
                    .background(
                        color = Purple80,
                        shape = TriangleRightEdgeShape(10)
                    )
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
        }
    }
}

class TriangleRightEdgeShape(private val offset: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = 0f, y = size.height - offset)
            lineTo(x = 0f, y = size.height)
            lineTo(x = 0f + offset, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}
