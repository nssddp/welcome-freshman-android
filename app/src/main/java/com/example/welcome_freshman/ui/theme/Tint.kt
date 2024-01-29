package com.example.welcome_freshman.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import javax.annotation.concurrent.Immutable

/**
 *@date 2024/1/28 10:28
 *@author GFCoder
 */
@Immutable
data class TintTheme(
    val iconTint: Color = Color.Unspecified,
)

val LocalTintTheme = staticCompositionLocalOf { TintTheme() }