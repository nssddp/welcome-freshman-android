package com.example.welcome_freshman.feature.certification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 *@date 2024/1/30 11:23
 *@author GFCoder
 */

@Composable
fun CertificationDialog(
    onAuthenticationClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = { Text(text = "学生认证", style = MaterialTheme.typography.titleLarge) },
        text = {
            Divider()
            Column {
                CertificationRow(text = "人脸识别认证") {
                    onAuthenticationClick("1")
                }
                Divider()
                CertificationRow(text = "身份证识别") {
                    onAuthenticationClick("2")
                }
                Divider()
                CertificationRow(text = "入学通知书识别") {
                    onAuthenticationClick("3")
                }
                Divider()
            }
        },
        confirmButton = {
            Text(text = "关闭", style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}


@Composable
fun CertificationRow(text: String, onAuthenticationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAuthenticationClick() }
            .padding(vertical = 12.dp),
        Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
        Icon(
            imageVector = Icons.Rounded.ArrowForwardIos,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
    }
}