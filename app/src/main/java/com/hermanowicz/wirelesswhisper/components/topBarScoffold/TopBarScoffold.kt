package com.hermanowicz.wirelesswhisper.components.topBarScoffold

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScaffold(
    topBarText: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit) = {
    },
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = topBarText,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = navigationIcon,
            actions = actions
        )
    }, content = { _ ->
            Column(modifier = Modifier.padding(top = 60.dp)) {
                content()
            }
        }, bottomBar = {
            bottomBar()
        })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopBarScaffoldLazyColumn(
    topBarText: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit) = {
    },
    bottomBar: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    TopBarScaffold(
        topBarText = topBarText,
        actions = actions,
        navigationIcon = navigationIcon,
        bottomBar = bottomBar,
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = LocalSpacing.current.small,
                        bottom = LocalSpacing.current.small,
                        start = LocalSpacing.current.small,
                        end = LocalSpacing.current.small
                    ),
                content = content
            )
        }
    )
}
