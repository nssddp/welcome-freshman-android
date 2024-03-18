package com.example.welcome_freshman.core.data.repository

import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.network.WfNetworkDataSource
import com.example.welcome_freshman.core.notification.Notifier
import javax.inject.Inject

/**
 *@date 2024/3/11 10:59
 *@author GFCoder
 */

interface TaskRepository {
    suspend fun getTasksByUserId(id: Int?): List<Task>?

}

class MainTaskRepository @Inject constructor(
    private val network: WfNetworkDataSource,
    private val notifier: Notifier,
) : TaskRepository {
    override suspend fun getTasksByUserId(id: Int?): List<Task>? = network.getTasksByUserId(id)

}

