package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/3/6 19:34
 *@author GFCoder
 */

@Serializable
data class Task(
    val taskId: Int,

    val taskName: String? = null,

    val taskType: String? = null,

    @SerialName("taskPoint")
    var taskValue: Int? = null,

    val parentId: Int? = null,

    val isMainline: Int? = null,
    /**
     * 任务描述
     */
    @SerialName("taskDescription")
    val description: String? = null,

    /**
     * 剩余时间
     */
    @SerialName("endTime")
    val validTime: String? = null,

    var progress: Float? = null,

    @SerialName("subTasks")
    val subTaskList: List<SubTask>? = null,

    val completionStatus: List<Float>? = null,

    val region: List<Region>? =null,

    val regionId: Int? =null


)


@Serializable
data class SubTask(
    @SerialName("taskId")
    val subTaskId: Int? = null,

    @SerialName("taskName")
    val subTaskName: String? = null,

    @SerialName("taskDescription")
    val subTaskDescription: String? = null,

    @SerialName("taskType")
    val subTaskType: String? = null,

    @SerialName("taskPoint")
    val subTaskScore: Int? = null,

//    @SerialName("sub_task_status")
    val subTaskStatus: String? = null,

    /*@SerialName("region")
    val taskCoordinate: Coordinate? = null,*/


    val centerLongitude: Double? = null,


    val centerLatitude: Double? = null,


    val punchType: String? = null,

    val regionId: Int? =null


    )

@Serializable
data class Coordinate(val latitude: Double, val longitude: Double)

@Serializable
data class PunchTask(
    val facecheck: Boolean? = null,
    val locationcheck: Boolean? = null
)


@Serializable
@SerialName("RegionEntity")
data class Region(
    val id: Int? = null,

    val regionName: String? = null
)
