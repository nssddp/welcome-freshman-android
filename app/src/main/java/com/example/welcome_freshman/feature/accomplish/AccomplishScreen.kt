package com.example.welcome_freshman.feature.accomplish

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.R
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme

/**
 *@date 2024/3/5 13:51
 *@author GFCoder
 */

@Composable
fun AccomplishRoute(onBackClick: () -> Unit) {
    AccomplishScreen()
}

@Composable
fun AccomplishScreen() {

    var value by remember {
        mutableStateOf(TextFieldValue())
    }
    val maxLength = 180

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.mission_completed),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 20.dp)
            )
            Text(
                text = "任务已完成",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            Text(
                text = "您的评价让任务变得更好",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 25.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = { if (it.text.length <= maxLength) value = it },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                maxLines = 7,
                placeholder = {
                    Text(
                        text = "添加评论......",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F1F3),
                    unfocusedContainerColor = Color(0xFFF1F1F3)
                )
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun AccomplishPreview() {
    WelcomeFreshmanTheme {
        AccomplishScreen()
    }
}