package com.example.welcome_freshman.feature.detail

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.SubTask
import com.example.welcome_freshman.feature.comment.CommentBottomSheet
import com.example.welcome_freshman.feature.main.task.TaskSection
import com.example.welcome_freshman.ui.component.AnimatedCircle
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@date 2024/1/28 9:32
 *@author GFCoder
 */

@Composable
fun DetailRoute(
    onBackClick: () -> Unit,
    onAccomplishClick: (String, String?) -> Unit,
    onQuizClick: (Int) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.taskDetailUiState.collectAsState()
//    LaunchedEffect(true) {
//        viewModel.getTaskDetail()
//    }
    DetailScreen(
        onBackClick = onBackClick,
        onCommentClick = { id, content -> viewModel.doComment(id, content) },
        onAccomplishClick = onAccomplishClick,
        onQuizClick = onQuizClick,
        uiState = uiState
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit = {},
    onAccomplishClick: (String, String?) -> Unit,
    onQuizClick: (Int) -> Unit,
    uiState: TaskDetailUiState? = null,
    onCommentClick: (Int, String) -> Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    var isOpenWrite by rememberSaveable { mutableStateOf(false) }

    val focusRequester = remember {
        FocusRequester()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "任务详情", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (uiState is TaskDetailUiState.Success)
                DetailBottomBar(
                    onCommentClick = {
                        openBottomSheet = true
                    },
                    onWriteClick = {
                        isOpenWrite = true
                        scope.launch {
                            delay(100)
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }

                    }
                )
        }
    ) { padding ->

        when (uiState) {
            TaskDetailUiState.Loading -> {}
            is TaskDetailUiState.Success -> {
                val taskDetail = uiState.taskDetail

                val state = rememberScrollState()

                Column(
                    Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(state),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(// 进度圈
                        modifier = Modifier
//                    .padding(16.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF8DA2EE),
                                        Color(0xFFDDE4FF).copy(0.2f)
                                    )
                                )
                            )
                    ) {
                        if (!taskDetail.completionStatus.isNullOrEmpty()) {
                            AnimatedCircle(
                                proportion = taskDetail.completionStatus[0],
                                color = Color(0xFF1A5ABA),

                                Modifier
                                    .height(200.dp)
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                            Column(modifier = Modifier.align(Alignment.Center)) {
                                Text(
                                    text = "当前进度",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "${taskDetail.completionStatus[0] * 100}%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    var isTaskCompleted by remember { mutableStateOf(false) } // 假设这是表示任务完成状态的变量
                    TaskSection(
                        title = R.string.task_name_title,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = taskDetail.taskName ?: "",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    TaskSection(
                        title = R.string.task_describe_title,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = taskDetail.description ?: "",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    var completedTasks: Pair<MutableList<SubTask>, MutableList<SubTask>>? by remember {
                        mutableStateOf(null)
                    }
                    if (!taskDetail.subTaskList.isNullOrEmpty() && taskDetail.completionStatus?.size!! > 1) {
                        completedTasks =
                            getTwoList(taskDetail.subTaskList, taskDetail.completionStatus)


                        TaskDetailSection(
                            title = R.string.task_in_progress_title,
                            taskNum = completedTasks!!.first.size.toString()
                        ) {

                            completedTasks?.first?.fastForEachIndexed {index,it  ->
                                val region = taskDetail.region?.find { region -> region.id == it.regionId }


                                SubTaskCard(
                                    isTaskCompleted = { false },
                                    subTaskTitle = it.subTaskName ?: "",
                                    subTaskType = it.subTaskType ?: "",
                                    punchType = it.punchType ?: "",
                                    location =region?.regionName  ,
                                    content = it.subTaskDescription ?: "",
                                    onAccomplishClick = { cameraId ->
                                        if (cameraId == "2")
                                            onQuizClick(it.subTaskId!!)
                                        else onAccomplishClick(
                                            cameraId,
                                            it.subTaskId.toString()
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.padding(vertical = 10.dp))

                            }

                        }
                        TaskDetailSection(
                            title = R.string.task_completed_title,
                            taskNum = completedTasks!!.second.size.toString()
                        ) {
                            completedTasks?.second?.fastForEach {

                                val region = taskDetail.region?.find { region -> region.id == it.regionId }

                                SubTaskCard(
                                    isTaskCompleted = { true },
                                    subTaskTitle = it.subTaskName ?: "",
                                    subTaskType = it.subTaskType ?: "",
                                    location = region?.regionName ?: "",
                                    content = it.subTaskDescription ?: "",
                                    punchType = ""
                                )
                                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                            }

                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 12.dp))
                    /*Button(onClick = { onAccomplishClick("1") }) {
                        Text(text = "go to completedScreen")
                    }*/
                }
                if (openBottomSheet) {
                    CommentBottomSheet(
//                openBottomSheet = openBottomSheet,
                        taskId = taskDetail.taskId,
                        openBottomSheetChanged = { openBottomSheet = it }
                    )
                }

                if (isOpenWrite)
                    ModalBottomSheet(
                        onDismissRequest = { isOpenWrite = false },
                        windowInsets = WindowInsets(0, 0, 0, 0),
//                sheetState = sheetState,
                        shape = MaterialTheme.shapes.medium,
                        dragHandle = {},
                    ) {
                        var value by remember { mutableStateOf("") }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = value,
                                onValueChange = { value = it },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    //                        .height(20.dp)
                                    .weight(0.1f)
                                    .focusRequester(focusRequester),
                                placeholder = {
                                    Text(
                                        text = "快来发表评论吧 ~",
                                        color = Color.Gray
                                    )
                                },
                                keyboardActions = KeyboardActions(onDone = {
                                    onCommentClick(taskDetail.taskId, value)
                                    isOpenWrite = false
                                }),
                                shape = MaterialTheme.shapes.extraLarge,
                                colors = TextFieldDefaults.colors(
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                )
                            )

                            IconButton(onClick = {
                                onCommentClick(taskDetail.taskId, value)
                                isOpenWrite = false

                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null
                                )
                            }

                        }

                    }

            }

            TaskDetailUiState.Error -> {}
            null -> {}
        }
//        val windowInsets by rememberUpdatedState(newValue = WindowInsets.isImeVisible)


    }

}

fun getTwoList(
    list: List<SubTask>,
    status: List<Float>
): Pair<MutableList<SubTask>, MutableList<SubTask>> {
    val completedList = mutableListOf<SubTask>()
    val uncompletedList = mutableListOf<SubTask>()

    for (i in 1 until status.size) {
        if (status[i].toInt() == 1) completedList.add(list[i - 1])
        else uncompletedList.add(list[i - 1])
    }

    return Pair(uncompletedList, completedList)
}


@Composable
fun TaskDetailSection(
    @StringRes title: Int,
    taskNum: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
                text = taskNum,
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
    subTaskType: String,
    punchType: String,
    subTaskTitle: String,
    location: String?,
    content: String,
    onAccomplishClick: (String) -> Unit = {  },
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
                    text = subTaskTitle,
                    style = MaterialTheme.typography.titleMedium,
                )



                Spacer(Modifier.weight(1f))

                if (isTaskCompleted()) {
                    // 任务完成时显示的标记，比如一个勾选图标
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "任务完成",
                        tint = Color(0xFF97D64E),
                        modifier = Modifier.size(24.dp)
                    )
                } /*else {
                    // 任务未完成时显示的内容
                    if (!location.isNullOrEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "地点: $location", style = MaterialTheme.typography.labelMedium)
                    }
                }*/
            }
            HorizontalDivider(Modifier.padding(bottom = 8.dp))
            Text(
                text = if (isTaskCompleted()) "恭喜，任务已完成！" else content/*"这是一个到指定地点打卡的任务啊啊啊啊".repeat(20)*/,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                color = Color.Gray
            )
            if (!location.isNullOrEmpty()) {
                Row(modifier= Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "地点: $location", style = MaterialTheme.typography.labelMedium)
                }

            }

            if (!isTaskCompleted()) {
                Button(
                    onClick = {
                        when (punchType) {
                            "Quiz" -> onAccomplishClick("2")
                            "Location" -> onAccomplishClick("1")
                            "OCR" -> onAccomplishClick("3")
                        }

                        /*if (subTaskType == "Quiz")
                            onAccomplishClick("1", null)
                        else onAccomplishClick("")*/
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


@Composable
fun DetailBottomBar(onCommentClick: () -> Unit = {}, onWriteClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 点赞图标
            IconButton(onClick = { /* 处理点赞逻辑 */ }) {
                Icon(Icons.Filled.Favorite, contentDescription = "点赞")
            }
            // 评论图标
            IconButton(onClick = onCommentClick) {
                Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = "评论")
            }

            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .weight(.6f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onWriteClick()
                    },
                color = Color(0x0F535252)
            ) {
                Text(
                    text = "写评论...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    WelcomeFreshmanTheme {
//        DetailScreen()
    }
}


