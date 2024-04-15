package com.example.welcome_freshman.feature.quiz

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.welcome_freshman.R
import com.example.welcome_freshman.feature.main.task.DialogAD
import com.example.welcome_freshman.ui.component.GainWardDialog
import com.example.welcome_freshman.ui.component.IndeterminateCircularIndicator
import com.example.welcome_freshman.ui.component.NetworkErrorIndicator
import com.example.welcome_freshman.ui.theme.OptionGreen
import com.example.welcome_freshman.ui.theme.OptionRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@date 2024/4/7 23:56
 *@author GFCoder
 */

@Composable
fun QuizRoute(
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
    onCompleted: () -> Unit,
) {
    val quizUiState by viewModel.quizUiState.collectAsState()
    val adUrl by viewModel.adUrl.collectAsState()

    QuizScreen(
        uiState = quizUiState,
        onBackClick = onBackClick,
        onCompleted = {
            if (it)
                viewModel.QuizCompleted(it)
            onCompleted()
        },
        onRetryClick = {
            viewModel.getQuizList()
        },
        adUrl = adUrl,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onBackClick: () -> Unit,
    onCompleted: (Boolean) -> Unit,
    onRetryClick: () -> Unit,
    adUrl: String?,
) {

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "答题界面",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (uiState) {
            QuizUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    IndeterminateCircularIndicator()

                }
            }

            is QuizUiState.Success -> {

                var correctCount by remember {
                    mutableIntStateOf(0)
                }
                var completeQuizCount by remember {
                    mutableIntStateOf(0)
                }

                val quizList = uiState.quizList

                val progress = remember {
                    mutableFloatStateOf(0f)
                }
//                progress.floatValue = (curQuiz + 1).toFloat() / quizList.size

                val pagerState = rememberPagerState { quizList.size }

                val isCompleted by remember {
                    derivedStateOf { completeQuizCount == quizList.size }
                }
                var shouldShowDialogAD by rememberSaveable {
                    mutableStateOf(false)
                }

                if (isCompleted) {
                    if (correctCount == quizList.size) {
                        GainWardDialog(counts = 110, countChange = 20) {
                            shouldShowDialogAD = true
                        }


                        if (shouldShowDialogAD)
                            DialogAD(onDismiss = { onCompleted(true) }, adUrl = adUrl)

//    if (viewModel.shouldShowDialogAD) {
//        shouldShowDialogAD = true

//    }
                    } else
                        onCompleted(false)

                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(padding),
                    userScrollEnabled = false
                ) { index ->
                    progress.floatValue = (pagerState.currentPage + 1).toFloat() / quizList.size

                    val isEnabled = rememberSaveable {
                        mutableStateOf(true)
                    }

                    LazyColumn(
                        modifier = Modifier
//                            .padding(padding)
                            .padding(horizontal = 24.dp)
                            .fillMaxSize()
                    ) {

                        item {
                            QuizSection(
                                quizTitle = quizList[index].quiz?.quizName ?: "Question",
                                totalQuiz = quizList.size,
                                curQuiz = index + 1,
                                quizContent = quizList[index].quiz?.quizDescription ?: "-",
                                onPreviousClick = {
                                    if (index > 0)
                                        scope.launch { pagerState.scrollToPage(index - 1) }
                                },
                                onForwardClick = {
                                    if (index < quizList.size - 1)
                                        scope.launch { pagerState.scrollToPage(index + 1) }
                                },
                                progress = progress,
                                quizPic = quizList[index].quiz?.quizPic
                            )
                        }

                        items(quizList[index].options!!) { option ->
                            val isUnSelected = rememberSaveable {
                                mutableStateOf(true)
                            }
                            /*if (!isUnSelected.value) {
                                completeQuizCount++
                                if (option.isCorrect!! == 1) {
                                    correctCount++
                                }
                            }*/

                            //                        isUnSelected.value = option.isUnselected
                            OptionItem(
                                optionText = option.optionText ?: "",
                                isUnSelected = isUnSelected,
                                optionId = option.optionId!!,
                                isCorrect = option.isCorrect!!,
                                isEnabled = isEnabled
                            ) {
//                                option.isUnselected = isUnSelected
                                if (it) {
                                    completeQuizCount++
                                    if (option.isCorrect == 1) {
                                        correctCount++
                                    }
                                    scope.launch {
                                        delay(1000)
                                        pagerState.scrollToPage(pagerState.currentPage + 1)
                                    }

                                }
                            }
                        }

                    }

                }


            }

            QuizUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    NetworkErrorIndicator(onRetryClick = onRetryClick)

                }
            }
        }


    }
}

@Composable
fun SingleQuiz() {

}

@Composable
fun QuizSection(
    quizTitle: String = "Question",
    totalQuiz: Int = 2,
    curQuiz: Int = 1,
    quizPic: String?,
    progress: MutableFloatState,
    quizContent: String = "-",
    onForwardClick: () -> Unit,
    onPreviousClick: () -> Unit
) {

    // 题目、进度和切换按钮
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$quizTitle  $curQuiz/$totalQuiz",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null
            )

        }
        IconButton(onClick = onForwardClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
            )

        }

    }
    // 进度条
    LinearProgressIndicator(
        progress = { progress.floatValue },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(20.dp),
        color = Color(0XFFFF8000),
        strokeCap = StrokeCap.Round,
    )
    // 题目内容
    Text(
        text = quizContent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    if (quizPic != null)
        AsyncImage(
            model = quizPic,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )

}

@Composable
fun OptionItem(
    optionText: String = "question",
    optionId: Int,
    isCorrect: Int,
    isUnSelected: MutableState<Boolean>,
    isEnabled: MutableState<Boolean>,
    onOptionSelected: (Boolean) -> Unit = {}
) {

    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(50.dp)
            .shadow(3.dp, shape = MaterialTheme.shapes.small, clip = true)
            .background(
                if (isUnSelected.value) Color.White else {
                    if (isCorrect == 1) OptionGreen else OptionRed
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = isEnabled.value
            ) {
                isUnSelected.value = false
                isEnabled.value = false
                onOptionSelected(true)
            },
    ) {
        Text(
            text = optionText,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterStart),
            color = if (!isUnSelected.value) Color.White else Color.Black,
            fontSize = 16.sp,
        )
        if (!isUnSelected.value)
            Icon(
                painter = painterResource(id = if (isCorrect == 1) R.drawable.option_correct else R.drawable.option_error),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(Color.White),
                tint = if (isCorrect == 1) OptionGreen else OptionRed
            )


    }

}

enum class OptionState {
    UNSELECTED,
    ERROR,
    CORRECT
}

@Preview
@Composable
private fun QuizPreview() {

//    QuizScreen()
}
