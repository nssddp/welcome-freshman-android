package com.example.welcome_freshman.ui.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll

/**
 *@date 2024/3/18 23:22
 *@author GFCoder
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToReFreshBox(state: PullToRefreshState, content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.nestedScroll(state.nestedScrollConnection)) {
        content()

        val scaleFraction = if (state.isRefreshing) 1f else
            LinearOutSlowInEasing.transform(state.progress).coerceIn(0f, 1f)

        PullToRefreshContainer(
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction)
        )

    }

}

