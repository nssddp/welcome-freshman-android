package com.example.welcome_freshman.data.repository

import com.example.welcome_freshman.data.model.Task
import com.example.welcome_freshman.data.network.WfNetworkDataSource
import javax.inject.Inject

/**
 *@date 2024/3/11 10:59
 *@author GFCoder
 */

interface TaskRepository {
    suspend fun getTasksByUserId(id: Int?): List<Task>

}

class MainTaskRepository @Inject constructor(
    private val network: WfNetworkDataSource

) : TaskRepository {
    override suspend fun getTasksByUserId(id: Int?): List<Task> = network.getTasksByUserId(id)

}

