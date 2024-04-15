package com.example.welcome_freshman.feature.AiChat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel

/**
 *@date 2024/4/11 22:15
 *@author GFCoder
 */

@Preview(showBackground = true)
@Composable
private fun ChatDialogPreview() {

//    ChatDialog(onDismissRequest = {})


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDialog(
    viewModel: ChatViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
//    viewModel.chatReq()
    val config = LocalConfiguration.current
    val messages = viewModel.messages.collectAsState().value
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 130.dp),
        onDismissRequest = { onDismissRequest() },
        title = {
            Row {
                Text(text = "AI小助手", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onDismissRequest() }) {
                    Icon(imageVector = Icons.Filled.Cancel, contentDescription = null)
                }

            }
        },
        text = {
            Column(
                Modifier
                    .fillMaxSize()
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(2.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // 聊天消息列表
                LazyColumn {
                    items(messages) { message ->
                        ChatMessageItem(message)
                    }
                }
                // 输入框和发送按钮
                ChatInput(
                    onSendMessage = { text ->
//                        onSendMessage(text)
                        viewModel.chatReq(text)
                        viewModel.sendMessage(ChatMessage("我", text, true))
                    }
                )
            }

        },
        confirmButton = {}
    )

}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (message.isSender) {
            // 如果是发送者消息，靠右对齐
            Text(
                text = message.chatName,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message.text,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
            )
        } else {
            // 如果是接收者消息，靠左对齐
            Text(
                text = message.chatName,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = message.text,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
//                backgroundColor = MaterialTheme.colorScheme.surface,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ChatInput(onSendMessage: (String) -> Unit) {
    var value by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("输入消息") },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    onSendMessage(value)
                    value = ""
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "发送消息"
                    )
                }
            },
            keyboardActions = KeyboardActions(onSend = {
                onSendMessage(value)
                value = ""
            }),

            )
    }
}


// 聊天消息的数据模型
data class ChatMessage(val chatName: String = "猫meme", val text: String, val isSender: Boolean)