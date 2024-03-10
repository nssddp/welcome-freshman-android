package com.example.welcome_freshman.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.R

/**
 *@date 2024/3/9 14:18
 *@author GFCoder
 */
@Composable
fun NetworkErrorIndicator(modifier: Modifier = Modifier,onRetryClick: () -> Unit = {},) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onRetryClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.connect_error),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
        )
        Text(text = "加载失败,点击重试", color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun indicatorPreview() {
    NetworkErrorIndicator()
}