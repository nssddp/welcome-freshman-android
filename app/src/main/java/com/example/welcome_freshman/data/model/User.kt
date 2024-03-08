package com.example.welcome_freshman.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:19
 *@author GFCoder
 */

@Serializable
data class User(
    @SerialName("user_id")
    val userId: Int,

    @SerialName("user_name")
    val userName: String,

    val gender: String,
    /**
     * 头像
     */
    @SerialName("avatar_url")
    val avatarUrl: String,
    /**
     * 积分
     */
    val score: Int,
    /**
     * 等级
     */
    val garde: Int,
    /**
     * 学院
     */
    val academy: String
)

@Serializable
data class LoginRequest(
    val stuId: Int,
    val password: String,
)