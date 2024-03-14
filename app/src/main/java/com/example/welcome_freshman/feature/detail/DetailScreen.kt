package com.example.welcome_freshman.feature.detail

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.feature.main.task.TaskSection
import com.example.welcome_freshman.ui.component.AnimatedCircle
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme

/**
 *@date 2024/1/28 9:32
 *@author GFCoder
 */

@Composable
fun DetailRoute(onBackClick: () -> Unit, onAccomplishClick: (String) -> Unit,viewModel: DetailViewModel = hiltViewModel()) {

    DetailScreen(onBackClick = onBackClick, onAccomplishClick = onAccomplishClick)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(onBackClick: () -> Unit = {}, onAccomplishClick: (String) -> Unit = {},) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "任务详情", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { padding ->

        val state = rememberScrollState()

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                AnimatedCircle(
                    proportion = 1f,
                    color = Color(0xFF1A5ABA),
                    /*listOf (
                    Color(0xFFBA1A1A), Color(0xFF8ABA1A), Color(
                0xFF6A1ABA
            )
        )*/
                    Modifier
                        .height(200.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "当前进度",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "%${1f * 100}",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            var isTaskCompleted by remember { mutableStateOf(false) } // 假设这是表示任务完成状态的变量
            TaskSection(
                title = R.string.task_name_title,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "renwu",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            TaskSection(title = R.string.task_describe_title, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "renwu",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            TaskDetailSection(title = R.string.task_in_progress_title) {
                SubTaskCard(
                    isTaskCompleted = { false },
                    subTaskTitle = null,
                    location = null,
                    content = ""
                )
            }
            TaskDetailSection(title = R.string.task_completed_title) {
                SubTaskCard(
                    isTaskCompleted = { true },
                    subTaskTitle = null,
                    location = null,
                    content = ""
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.Transparent
            )
            /*Button(onClick = { onAccomplishClick("1") }) {
                Text(text = "go to completedScreen")
            }*/
        }
    }

}


@Composable
fun TaskDetailSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VerticalDivider(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(24.dp),
                color = MaterialTheme.colorScheme.primary,
                thickness = 3.dp
            )
            Text(
                stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
            Canvas(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(4.dp)
            ) {
                drawCircle(Color(0xFF3C5BA9))
            }
            Text(
                text = "1",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        content()
    }
}

@Composable
fun SubTaskCard(
    isTaskCompleted: () -> Boolean,
    subTaskTitle: String?,
    location: String?,
    content: String,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .animateContentSize()
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                expanded = !expanded
            },
        colors = CardDefaults.elevatedCardColors(containerColor = if (isTaskCompleted()) Color.LightGray else MaterialTheme.colorScheme.surface)// 如果任务完成，改变背景色
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "打卡任务",
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(Modifier.weight(1f))

                if (isTaskCompleted()) {
                    // 任务完成时显示的标记，比如一个勾选图标
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "任务完成",
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    // 任务未完成时显示的内容
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "任务完成",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "地点: 第四教学楼", style = MaterialTheme.typography.labelLarge)
                }
            }
            HorizontalDivider(Modifier.padding(bottom = 8.dp))
            Text(
                text = if (isTaskCompleted()) "恭喜，任务已完成！" else "这是一个到指定地点打卡的任务啊啊啊啊".repeat(
                    20
                ),
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                color = Color.Gray
            )
            if (!isTaskCompleted()) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier.padding(top = 16.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isTaskCompleted()) Color.Gray else MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "打卡",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }

    }

}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    WelcomeFreshmanTheme {
        DetailScreen()
    }
}
