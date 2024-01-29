package com.example.welcome_freshman.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 *@date 2024/1/28 9:32
 *@author GFCoder
 */

@Composable
fun DetailRoute() {
    DetailScreen()
}

@Composable
fun DetailScreen() {
    Box(Modifier.fillMaxSize()) {
        Text(text = "This is detailScreen", modifier = Modifier.align(Alignment.Center))
    }
}
