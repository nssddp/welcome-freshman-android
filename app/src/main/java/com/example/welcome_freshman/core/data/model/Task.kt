package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:34
 *@author GFCoder
 */

@Serializable
data class Task(
    @SerialName("task_id")
    val taskId: Int,

    @SerialName("task_name")
    val taskName: String? = null,

    @SerialName("task_type")
    val taskType: String? = null,

    @SerialName("task_value")
    val taskValue: Int? = null,
    /**
     * 任务描述
     */
    val description: String? = null,
    /**
     * 剩余时间
     */
    @SerialName("valid_time")
    val validTime: String? = null,

    val progress: Int? = null,

    val subTaskList: List<SubTask>? = null
)

@Serializable
data class SubTask(
    @SerialName("sub_task_id")
    val subTaskId: Int? = null,

    @SerialName("sub_task_name")
    val subTaskName: String? = null,

    @SerialName("sub_task_status")
    val subTaskStatus: String? = null,

    @SerialName("task_coordinate")
    val taskCoordinate: Coordinate? = null,

)

@Serializable
data class Coordinate(val latitude: Double, val longitude: Double)
