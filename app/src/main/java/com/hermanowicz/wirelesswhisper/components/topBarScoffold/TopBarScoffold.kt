package com.hermanowicz.wirelesswhisper.components.topBarScoffold

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopBarScaffold(
    topBarText: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = topBarText)
            },
            navigationIcon = navigationIcon,
            actions = actions,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 10.dp
        )
    }, content = {
            content()
        }, bottomBar = {
            bottomBar()
        })
}
