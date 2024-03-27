package com.example.welcome_freshman.feature.ad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 *@date 2024/3/18 16:20
 *@author GFCoder
 */
@Composable
fun AdScreen(count: () -> Int, adUrl: () -> String?,onSkipClick: () -> Unit) {


    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            model = adUrl,
            contentDescription = "ad",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Button(
            onClick = onSkipClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .statusBarsPadding()
                .alpha(0.4f)
        ) {
            Text(
                text = "跳过 ${count()}", style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AdScreenPreview() {
//    AdScreen()
}