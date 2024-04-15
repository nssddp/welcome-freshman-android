package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/4/3 16:02
 *@author GFCoder
 */

@Serializable
data class Comment(
    val userId: Int? = null,
    val taskId: Int? = null,
    @SerialName("communityId")
    val commentId: Int? = null,
    @SerialName("text")
    val content: String? = null,
    val account: String? = null,
    val head: String? = null,
    val praise: Int? = 0,
    val view: Int? = 0,
    val createTime: Long? = null,
)
