package com.example.welcome_freshman.feature.task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@Composable
fun TaskRoute() {
    TaskScreen()
}

@Composable
fun TaskScreen() {
    Text(text = "This is Task Screen", modifier = Modifier.fillMaxSize())
}
