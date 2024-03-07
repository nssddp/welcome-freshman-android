package com.example.welcome_freshman.data.model

import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:19
 *@author GFCoder
 */
data class User(
    val id: Int,
    val userName: String,
    val gender: String,
    val avatarUrl :String,
    val score: Int,
    val garde: Int,
    val academy: String
)