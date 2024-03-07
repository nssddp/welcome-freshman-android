package com.example.welcome_freshman.data.model

import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:34
 *@author GFCoder
 */
data class Task(
    val id: Int,
    val taskName: String,
    val describe: String,
)
