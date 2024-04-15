package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/30 18:35
 *@author GFCoder
 */

@Serializable
data class HomeDto(

    val stuName: String? = null,

    @SerialName("tasksEntities")
    val tasks : List<Task>? = null,

    @SerialName("progress")
    val progress : List<Progress>? = null,

)

@Serializable
data class Progress(
    val taskId: Float? = null,
    val progress: Float? =null,
)


