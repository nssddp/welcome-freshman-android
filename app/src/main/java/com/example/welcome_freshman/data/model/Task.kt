package com.example.welcome_freshman.data.model

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
    val taskName: String,

    @SerialName("task_type")
    val taskType: String,

    @SerialName("task_value")
    val taskValue: Int,
    /**
     * 任务描述
     */
    val description: String,
    /**
     * 剩余时间
     */
    @SerialName("rest_time")
    val validTime: String,

    val progress: Int,

    val subTaskList: List<SubTask>
)

@Serializable
data class SubTask(
    @SerialName("sub_task_id")
    val subTaskId: Int,

    @SerialName("sub_task_name")
    val subTaskName: String,

    @SerialName("sub_task_status")
    val subTaskStatus: String,

    @SerialName("task_coordinate")
    val taskCoordinate: Coordinate

)

@Serializable
data class Coordinate(val latitude: Double, val longitude: Double)
