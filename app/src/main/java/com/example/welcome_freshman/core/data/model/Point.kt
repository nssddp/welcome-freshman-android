package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/30 18:36
 *@author GFCoder
 */
@Serializable
data class Point(
    val taskId: Int? = null,
    val completionStatus: Float? = null,
    val pointsEarned: Int? = null,
)
