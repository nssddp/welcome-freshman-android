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

    val rank: Int? = null,
    /**
     * 头像
     */
    @SerialName("head")
    var avatarUrl: String? = null,
    /**
     * 积分
     */
    @SerialName("point")
    val score: Int? = null,
    /**
     * 等级
     */
    val grade: Int? = null,
    /**
     * 学院
     */
    @SerialName("name")
    val academy: String? = null,

    /**
     * 激活状态
     */
    @SerialName("preferences")
    val validState: String? = null,

    /**
     * 完成任务的平均时间
     */
    val aveTime: Float? = null,

    /**
     * 单项任务完成时间
     */
    val completionTime: Long? = null,

    val strength: Int? = null,

    val agility: Int? = null,

    val intelligence: Int? = null,

    val emp: Int? = null,
)

@Serializable
data class LoginRequest(
    val studentNumber: Int,
    val password: String,
)