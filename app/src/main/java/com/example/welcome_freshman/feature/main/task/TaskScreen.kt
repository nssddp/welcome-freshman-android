package com.example.welcome_freshman.feature.main.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme

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
    var selected by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilterChip(chipName = "主线任务", selected = { !selected }) {
                    selected = !selected
                }

                TaskFilterChip(chipName = "支线任务", selected = { selected }) {
                    selected = !selected
                }
            }
        }
        items(sampleData) {
            TaskListItem(taskTitle = "一个good任务", "剩余12小时", content = it)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFilterChip(chipName: String, selected: () -> Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected(), onClick = onClick,
        label = {
            Text(text = chipName)
        },
        leadingIcon = {
            if (selected()) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        }
    )
}

@Composable
fun TaskListItem(
    taskTitle: String,
    validTime: String,
    content: String,
) {
    Box(Modifier.padding(horizontal = 16.dp)) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = taskTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                    )
                    Text(
                        text = content,
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = validTime,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, end = 12.dp)
                )

            }


        }
        TaskType(
            taskType = "主线任务", modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp)
        )

    }

}

@Composable
fun TaskType(modifier: Modifier, taskType: String) {
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.shapes.large
        )
    ) {
        Text(
            text = taskType,
            Modifier.padding(3.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

fun getSampleData(size: Int): List<String> {
    return (1..size).map { "This is Task $it" }
}


@Preview
@Composable
fun taskItem() {
    WelcomeFreshmanTheme {
        TaskListItem(taskTitle = "准备好通知书", content = "dd", validTime = "剩余12小时")
    }

}