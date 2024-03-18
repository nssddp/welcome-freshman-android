package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:19
 *@author GFCoder
 */

@Serializable
data class User(
    val userId: Int? = 0,

    @SerialName("account")
    val userName: String = "",

    val gender: String = "男",
    /**
     * 头像
     */
    @SerialName("head")
    var avatarUrl: String? = null,
    /**
     * 积分
     */
    val score: Int? = null,
    /**
     * 等级
     */
    val garde: Int? = null,
    /**
     * 学院
     */
    val academy: String? = null,

    /**
     * 激活状态
     */
    @SerialName("preferences")
    val validState: String? = null,
)

@Serializable
data class LoginRequest(
    val studentNumber: Int,
    val password: String,
)