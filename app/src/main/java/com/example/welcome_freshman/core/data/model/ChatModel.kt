package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/4/11 21:30
 *@author GFCoder
 */

@Serializable
data class ChatModel(

    val ques: String = "hello",
    // 官网-个人中心-Appkey获取
    @SerialName("appKey")
    val appKey: String = "6614efe89ebcf2d2145fba46",
    // 官网-个人中心-用户ID
    @SerialName("uid")
    val uid: String = "9I3eKM1712648168962UxJpwFoncV",
    // 是否支持上下文 值1表示支持,0表示不支持
    val isLongChat: Int = 0

)

@Serializable
data class AiData(

    val result: String? = null,

    val countMsg: String? = null,

    val longChat: Int? = null,
)
