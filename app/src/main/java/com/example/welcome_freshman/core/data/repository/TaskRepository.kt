package com.example.welcome_freshman.core.data.repository

import com.example.welcome_freshman.core.data.model.Comment
import com.example.welcome_freshman.core.data.model.HomeDto
import com.example.welcome_freshman.core.data.model.QuizDto
import com.example.welcome_freshman.core.data.model.SubTask
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.network.WfNetworkDataSource
import com.example.welcome_freshman.core.notification.Notifier
import javax.inject.Inject

/**
 *@date 2024/3/11 10:59
 *@author GFCoder
 */

interface TaskRepository {
    suspend fun getTasksByUserId(id: Int?): HomeDto?
    suspend fun getTaskDetail(userId: Int?, taskId: Int?): Task?

    suspend fun getCommentByTaskId(taskId: Int?): List<Comment>?
    suspend fun getQuizList(subTaskId: Int): List<QuizDto>?

    suspend fun quizCompleted(userId: Int?, taskId: Int?, res: Boolean): Boolean?
    suspend fun chatReq(request: Map<String, String>): String?

    suspend fun getRegionTask(): List<SubTask>?

    suspend fun checkLocation(
        pic: ByteArray,
        subTaskId: Int,
        stuId: Int,
        coordinate: Pair<Double, Double>
    ): Int

    suspend fun checkOnNotice(pic: ByteArray, userId: Int): Int
}

class MainTaskRepository @Inject constructor(
    private val network: WfNetworkDataSource,
    private val notifier: Notifier,
) : TaskRepository {
    override suspend fun getTasksByUserId(id: Int?): HomeDto? = network.getTasksByUserId(id)
    override suspend fun getTaskDetail(userId: Int?, taskId: Int?): Task? =
        network.getTaskDetail(userId, taskId)

    override suspend fun getCommentByTaskId(taskId: Int?): List<Comment>? =
        network.getCommentByTaskId(taskId)

    override suspend fun getQuizList(subTaskId: Int): List<QuizDto>? =
        network.getQuizList(subTaskId)

    override suspend fun quizCompleted(userId: Int?, taskId: Int?, res: Boolean): Boolean? =
        network.quizCompleted(userId, taskId, res)

    override suspend fun chatReq(request: Map<String, String>): String? =
        network.reqChat(request)

    override suspend fun getRegionTask(): List<SubTask>? = network.getRegionTask()
    override suspend fun checkLocation(
        pic: ByteArray,
        subTaskId: Int,
        stuId: Int,
        coordinate: Pair<Double, Double>
    ): Int = network.checkLocation(
        identity = pic, userId = stuId, taskId = subTaskId
    )

    override suspend fun checkOnNotice(pic: ByteArray, userId: Int): Int =network.checkOnNotice(pic,userId)


}

