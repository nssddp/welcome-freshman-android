package com.example.welcome_freshman.feature.main.task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.welcome_freshman.ui.component.WfNavigationDrawer

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

    val sampleData by remember {
        mutableStateOf(getSampleData(80))
    }

    LazyColumn(Modifier.fillMaxSize()){
        items(sampleData) {
            Text(text = it)
        }

    }
}


fun getSampleData(size: Int): List<String> {
    return (1..size).map { "This is Task $it" }
}
