package com.example.welcome_freshman.feature.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@Composable
fun ProfileRoute() {

    ProfileScreen()
}

@Composable
fun ProfileScreen() {
    Text(text = "This is Profile Screen", modifier = Modifier.fillMaxSize())

}