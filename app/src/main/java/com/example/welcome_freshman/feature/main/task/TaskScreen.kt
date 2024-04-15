package com.example.welcome_freshman.feature.main.task

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.Progress
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.feature.AiChat.ChatDialog
import com.example.welcome_freshman.ui.component.GifImage
import com.example.welcome_freshman.ui.component.IndeterminateCircularIndicator
import com.example.welcome_freshman.ui.component.NetworkErrorIndicator
import com.example.welcome_freshman.ui.component.PullToReFreshBox
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
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

    val adUrl by viewModel.adUrl.collectAsState()

    val refreshState = rememberPullToRefreshState()

    TaskScreen(
        onDetailClick = onDetailClick,
        uiState = uiState,
        refreshState = refreshState,
        getTasks = viewModel::getTasks,
        adUrl = adUrl,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    onDetailClick: (String) -> Unit = {},
    uiState: TaskUiState,
    refreshState: PullToRefreshState,
    getTasks: () -> Unit,
    adUrl: String?,
) {
    var tasks: List<Task> by remember {
        mutableStateOf(emptyList())
    }

    val sampleData by remember {
        mutableStateOf(getSampleData(80))
    }

    var filterType by rememberSaveable {
        mutableIntStateOf(0)
    }

    var userName by remember {
        mutableStateOf("")
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) { getTasks() }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    PullToReFreshBox(state = refreshState) {

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.home_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.95f
        )

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
                        .padding(horizontal = 16.dp),
                    userName = userName,
                )
            }

            // 广告
            item {
                Box(Modifier.padding(10.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = adUrl, placeholder = painterResource(
                                id = R.drawable.adpalceholder
                            )
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = "广告",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.background(
                            Color.White.copy(.4f),
                            shape = RoundedCornerShape(3.dp)
                        )
                    )


                }
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
                                    selected = { selectedFilterChip == chipName }
                                ) {
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
                    uiState.homeDto.stuName.let {
                        if (it != null) {
                            userName = it
                        }
                    }

                    if (uiState.homeDto.tasks != null && uiState.homeDto.progress != null) {
                        tasks = updateTaskProgress(uiState.homeDto.tasks, uiState.homeDto.progress)

                        when (filterType) {
                            0 -> {}

                            1 -> tasks = tasks.filter { task ->
                                task.isMainline == 0
                            }

                            2 -> tasks = tasks.filter { task ->
                                task.isMainline == 1
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
                                onDetailClick = onDetailClick,
                            )
                        }
                    }


                }

                TaskUiState.Error -> {
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
                Icon(imageVector = Icons.Rounded.KeyboardArrowUp, null)
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
        },
        colors = FilterChipDefaults.filterChipColors(containerColor = Color.White.copy(.6f))
    )
}

@Composable
fun TaskListItem(
    task: Task,
    onDetailClick: (String) -> Unit = {},
) {

    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                onDetailClick(task.taskId.toString())
            }
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(.6f))
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
                            text = "剩余${task.validTime?.let { getValidTime(it) } ?: ""} 小时",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp, end = 12.dp)
                        )
                        Text(
                            text = "价值: ${task.taskValue}",
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
                        progress = { task.progress ?: 0.0f },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 12.dp),
                    )
                    Text(
                        text = "${task.progress?.times(100) ?: (0.0f * 100)}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                }

            }

        }

        TaskType(
            taskType = task.taskType ?: "",
            isMain = task.isMainline ?: 0,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp)
        )

    }

}

@Composable
fun TaskType(modifier: Modifier, taskType: String, isMain: Int) {
    Box(
        modifier = modifier.background(
            if (isMain == 0) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.inversePrimary,
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
                .padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
fun HomeMainCard(modifier: Modifier = Modifier, userName: String) {
    val context = LocalContext.current
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


    var isShowChatDialog by remember {
        mutableStateOf(false)
    }


    OutlinedCard(
        modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.dp,
            Color.Black
        )
    ) {

        Text(
            text = formatted,
            Modifier.padding(start = 8.dp, top = 8.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "你好，$userName",
            Modifier.padding(start = 8.dp, top = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )


        Row(Modifier.fillMaxWidth()) {
            GifImage(
                painter = R.drawable.meme_cat,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isShowChatDialog = !isShowChatDialog
                }
            )

            Image(
                painter = painterResource(id = R.drawable.dialog_kuang),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isShowChatDialog = !isShowChatDialog
                    }
            )

        }

    }

    if (isShowChatDialog)
        ChatDialog(
            onDismissRequest = { isShowChatDialog = false }
        )
}

fun getSampleData(size: Int): List<String> {
    return (1..size).map { "This is Task $it" }
}

private fun getValidTime(endTime: String): String {
    // 获取当前时间
    val currentTime = LocalDateTime.now()

    // 指定目标时间
//    val targetTimeStr = "2024-03-01T00:00:00"
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val targetTime = LocalDateTime.parse(endTime, formatter)

    // 计算当前时间和目标时间之间的差值
    val duration = Duration.between(currentTime, targetTime)

    // 将差值转换为小时数
    val hours = duration.toHours()
    val remainingHours = if (duration.isNegative) 0 else hours
    return remainingHours.toString()
}


fun updateTaskProgress(tasks: List<Task>, progressList: List<Progress>): List<Task> {
    // 创建一个 taskId 到 progress 的映射，方便后续查找
    val progressMap = progressList.associateBy { it.taskId?.toInt() }

    // 遍历 tasks 列表，更新每个 Task 的 progress 属性
    return tasks.map { task ->
        // 从映射中查找对应的 progress 值
        val progressValue = progressMap[task.taskId] // 注意 taskId 是 Int 类型，需要转换为 Float
        // 创建一个新的 Task 实例，其中 progress 属性被更新
        task.copy(progress = progressValue?.progress)
    }
}

@Composable
fun DialogAD(onDismiss: () -> Unit, adUrl: String?) {
    AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = { /*TODO*/ }, text = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 300.dp, horizontal = 100.dp)
        ) {
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(imageVector = Icons.Filled.Cancel, contentDescription = null)
            }
            Image(
                painter = rememberAsyncImagePainter(model = adUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

    })
}


@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    WelcomeFreshmanTheme {
        /*TaskListItem(
            Task(1, "ggbone", "主线任务", 1, 0, 1, "这是一个任务")
        )*/
        HomeMainCard(
            Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp), userName = "老6"
        )

//        TaskScreen(uiState = null, refreshState = null)
    }

}