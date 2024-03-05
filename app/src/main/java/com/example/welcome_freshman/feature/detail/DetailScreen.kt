package com.example.welcome_freshman.feature.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.R
import com.example.welcome_freshman.ui.component.AnimatedCircle
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme

/**
 *@date 2024/1/28 9:32
 *@author GFCoder
 */

@Composable
fun DetailRoute(onBackClick: () -> Unit, onAccomplishClick: (String) -> Unit) {
    DetailScreen(onBackClick = onBackClick, onAccomplishClick = onAccomplishClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(onBackClick: () -> Unit = {}, onAccomplishClick: (String) -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "任务详情", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    AnimatedCircle(
                        proportions = listOf(0.25f, 0.25f, 0.25f, 0.25f),
                        colors = listOf(
                            Color(0xFFBA1A1A), Color(0xFF8ABA1A), Color(0xFF1A5ABA), Color(
                                0xFF6A1ABA
                            )
                        ),
                        Modifier
                            .height(200.dp)
                            .align(Alignment.Center)
                            .fillMaxWidth()
                    )
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "任务名称",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "当前进度",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                TaskDetailSection(title = R.string.task_in_progress_title) {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(80.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = "这是一个到指定地点打卡的任务....................................",
                                    Modifier.padding(start = 4.dp, top = 8.dp, end = 4.dp),
                                    style = MaterialTheme.typography.bodyLarge,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                                Row(Modifier.padding(start = 4.dp, bottom = 4.dp)) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = null
                                    )
                                    Text(text = "地点: 第四教学楼")
                                }

                            }

                        }
                        Button(
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier.height(80.dp)
                        ) {
                            Text(text = "打卡", style = MaterialTheme.typography.bodyMedium)
                        }

                    }
                }
                TaskDetailSection(title = R.string.task_completed_title) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "This is detailScreen")
                    }
                }

                Button(onClick = { onAccomplishClick("1") }) {
                    Text(text = "go to completedScreen")
                }
            }
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
            Divider(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(24.dp)
                    .width(3.dp),
                color = MaterialTheme.colorScheme.primary
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

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    WelcomeFreshmanTheme {
        DetailScreen()
    }
}
