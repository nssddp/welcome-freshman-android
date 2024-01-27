package com.example.welcome_freshman.feature.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

@Composable
fun ListRoute() {
    ListScreen()
}

@Composable
fun ListScreen() {
    Text(text = "This is List Screen", modifier = Modifier.fillMaxSize())
}

