package com.example.welcome_freshman.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.welcome_freshman.R

/**
 *@date 2024/3/16 10:53
 *@author GFCoder
 */
@Composable
fun ValidCircularIndicator(indicatorState: Int, onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .size(120.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            when (indicatorState) {
                0 -> {
                    IndeterminateCircularIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .padding(bottom = 20.dp),
                    )
                }

                1 -> {
                    /*val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.mission_completed
                        )
                    )
                    var isPlaying by remember { mutableStateOf(false) }
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        isPlaying = true,

                    )
                    LaunchedEffect(progress) {
                        if (progress == 0f) isPlaying = true
                        if (progress == 1f) isPlaying = false
                    }*/

                    Column(
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        /*LottieAnimation(
                            modifier = Modifier.size(80.dp),
                            composition = composition,
                            progress = progress
                        )*/
                        Image(
                            painter = painterResource(id = R.drawable.valid_success),
                            contentDescription = "valid_success",
                            modifier = Modifier
                                .width(76.dp)
                                .padding(bottom = 6.dp)
                        )
                        Text(text = "验证成功", style = MaterialTheme.typography.bodySmall)
                    }
                }

                2 -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.valid_fail),
                            contentDescription = "valid_success",
                            modifier = Modifier
                                .width(76.dp)
                                .padding(bottom = 6.dp)
                        )
                        Text(text = "验证失败, 请重试", style = MaterialTheme.typography.bodySmall)
                    }

                }
            }


        }
    }
}


@Preview
@Composable
fun ValidCircularIndicatorPreview() {
    ValidCircularIndicator(1)
}