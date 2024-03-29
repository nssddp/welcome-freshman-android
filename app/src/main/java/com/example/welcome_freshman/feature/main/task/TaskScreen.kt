package com.example.welcome_freshman.feature.main.task

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.ui.component.IndeterminateCircularIndicator
import com.example.welcome_freshman.ui.component.NetworkErrorIndicator
import com.example.welcome_freshman.ui.component.PullToReFreshBox
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskRoute(onDetailClick: (String) -> Unit = {}, viewModel: TaskViewModel = hiltViewModel()) {
    val uiState by viewModel.taskUiState.collectAsState()

    val refreshState = rememberPullToRefreshState()

    TaskScreen(
        onDetailClick = onDetailClick,
        uiState = uiState,
        refreshState = refreshState,
        getTasks = viewModel::getTasks
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    onDetailClick: (String) -> Unit = {},
    uiState: TaskUiState,
    refreshState: PullToRefreshState,
    getTasks: () -> Unit,
) {
    var tasks: List<Task> by remember {
        mutableStateOf(emptyList())
    }
    val sampleData by remember {
        mutableStateOf(getSampleData(80))
    }

    var filterType by remember {
        mutableStateOf(0)
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) { getTasks() }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    PullToReFreshBox(state = refreshState){
        LazyColumn(
            Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))
                HomeMainCard(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                Box {
                    TaskSection(title = R.string.today_task_title) {
                        val filterChipOptions = listOf("全部", "主线任务", "支线任务")
                        var selectedFilterChip by rememberSaveable {
                            mutableStateOf(filterChipOptions[0])
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filterChipOptions.fastForEachIndexed { index, chipName ->
                                TaskFilterChip(
                                    chipName = chipName,
                                    selected = { selectedFilterChip == chipName }) {
                                    selectedFilterChip = chipName
                                    filterType = index

                                }

                            }

                        }

                    }
                }
            }

            when (uiState) {
                TaskUiState.Loading -> {
                    if (!refreshState.isRefreshing) {
                        refreshState.startRefresh()
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                        IndeterminateCircularIndicator()
                    }
                }

                is TaskUiState.Success -> {
                    refreshState.endRefresh()
                    scope.launch {
                        when (filterType) {
                            0 -> tasks = uiState.tasks

                            1 -> tasks = uiState.tasks.filter { task ->
                                task.taskType == "主线任务"
                            }

                            2 -> tasks = uiState.tasks.filter { task ->
                                task.taskType == "支线任务"
                            }
                        }
                    }

                    items(items = tasks, key = { task ->
                        task.taskId
                    }) { task ->
                        TaskListItem(
                            task = task,
                            /*task.taskName,
                            task.taskType,
                            task.validTime,
                            task.description,*/
                            onDetailClick = onDetailClick
                        )
                    }
                }

                else -> {
                    refreshState.endRefresh()
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                        NetworkErrorIndicator(onRetryClick = { getTasks() })
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

        }
        // 返回顶部button

        AnimatedVisibility(showButton) {
            IconButton(
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp)
                    .align(Alignment.BottomEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    scope.launch { listState.scrollToItem(0) }
                }
            ) {
                Icon(imageVector = Icons.Rounded.ArrowUpward, null)
            }
        }
    }


}

@Composable
fun TaskFilterChip(chipName: String, selected: () -> Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected(), onClick = onClick,
        label = {
            Text(text = chipName, style = MaterialTheme.typography.labelMedium)
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
    task: Task,
    progress: Float = 0.7f,
    onDetailClick: (String) -> Unit = {},
) {
    val currentProgress by remember { mutableStateOf(progress) }

    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                onDetailClick("")
            }
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column {
                // 任务信息部分
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 左边部分
                    Column(Modifier.fillMaxWidth(0.7f)) {
                        Text(
                            text = task.taskName ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                        )
                        /*Text(
                            text = "任务位置: XXXXXX",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            maxLines = 1,
                        )*/
                        Text(
                            text = task.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp),
                            maxLines = 1,
                        )
                    }
                    // 右边部分
                    Column {
                        Text(
                            text = task.validTime ?: "",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp, end = 12.dp)
                        )
                        Text(
                            text = "价值:${task.taskValue}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp, end = 12.dp)
                        )
                    }

                }
                // 进度部分
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "任务进度:",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    LinearProgressIndicator(
                        progress = { currentProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 12.dp),
                    )
                    Text(text = "%${progress * 100}", style = MaterialTheme.typography.labelMedium)
                }

            }

        }

        TaskType(
            taskType = task.taskType ?: "", modifier = Modifier
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

@Composable
fun TaskSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

@Composable
fun HomeMainCard(modifier: Modifier) {
    val currentTime = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy年M月dd日 EEEE", Locale.CHINA)
    var formatted by remember {
        mutableStateOf(currentTime.format(formatter))
    }
    LaunchedEffect(key1 = true, block = {
        while (true) {
            val newDate = LocalDate.now().format(formatter)

            if (formatted != newDate) formatted = newDate

            delay(7200_000)
        }
    })


    OutlinedCard(modifier) {
        Text(
            text = formatted,
            Modifier.padding(start = 8.dp, top = 8.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "早上好，GGBone!",
            Modifier.padding(start = 8.dp, top = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

fun getSampleData(size: Int): List<String> {
    return (1..size).map { "This is Task $it" }
}


@Preview(showBackground = true)
@Composable
fun taskItem() {
    WelcomeFreshmanTheme {
        /*TaskListItem(
            taskTitle = "准备好通知书",
            content = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
            validTime = "剩余12小时",
            taskType = "主线任务"
        )*/

//        TaskScreen(uiState = null, refreshState = null)
    }

}