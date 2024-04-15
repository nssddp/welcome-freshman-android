package com.example.welcome_freshman.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.welcome_freshman.R
import kotlinx.coroutines.delay

/**
 *@date 2024/3/12 16:34
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WfDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    content: @Composable () -> Unit
) {
    BasicAlertDialog(onDismissRequest = { onDismissRequest() }, modifier = modifier) {
        content()

    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .size(120.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            IndeterminateCircularIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(bottom = 20.dp),
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GainWardDialog(counts: Int, countChange: Int, onDismissRequest: () -> Unit) {

    var count by remember {
        mutableIntStateOf(counts)
    }
    var oldCount by remember {
        mutableIntStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    LaunchedEffect(true) {
        for (i in 1..countChange) {
            count++
            if (countChange > 9)
                delay(200)
            else delay(500)
        }

    }

    val countString = count.toString()
    val oldCountString = oldCount.toString()

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(120.dp),
            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            // title
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(Color(0xFFC98625)),
            ) {

                Text(
                    text = "获得奖励",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Image(
                    painter = painterResource(id = R.drawable.gain_ward_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                        .size(20.dp)

                )
            }

            // 积分行
            Row(
                Modifier
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.point),
                    contentDescription = null
                )

                Text(
                    text = "获得积分:",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    textAlign = TextAlign.Center,
                    color = Color(0XFFFF9800)
                )
                Text(
                    text = "+$countChange (", color = Color(0XFFFF9800)
                )

                for (i in countString.indices) {
                    val oldChar = oldCountString.getOrNull(i)
                    val newChar = countString[i]
                    val char = if (oldChar == newChar) {
                        oldCountString[i]
                    } else {
                        countString[i]
                    }

                    AnimatedContent(
                        targetState = char,
                        transitionSpec = {
                            addAnimation(duration = if (countChange > 9) 200 else 400).using(
                                SizeTransform(true)
                            )
                        },
                        label = ""
                    ) { targetChar ->
                        Text(
                            text = targetChar.toString(),
                            color = Color(0XFFFF9800)
                        )
                    }

                }

                Text(
                    text = ")",
                    color = Color(0XFFFF9800)
                )

            }


            TextButton(onClick = onDismissRequest, modifier = Modifier.align(Alignment.End)) {
                Text(text = "确定", color = Color(0xFF0D8EC9))
            }

        }
    }

}

fun addAnimation(duration: Int = 200): ContentTransform {
    return slideInVertically(tween(duration)) { height -> -height } + fadeIn(
        tween(duration)
    ) togetherWith slideOutVertically(tween(duration)) { height -> height } + fadeOut(
        tween(duration)
    )
}

@Preview(showBackground = true)
@Composable
fun WfDialogPreview() {
    var shouldShowDialog by remember {
        mutableStateOf(true)
    }
    Box(modifier = Modifier.fillMaxSize()) {


        if (shouldShowDialog)
            GainWardDialog(
                counts = 100,
                countChange = 5,
                onDismissRequest = { shouldShowDialog = false })

        Button(onClick = { shouldShowDialog = true }) {

        }
    }

//    ChatDialog(onDismissRequest = {})
}

