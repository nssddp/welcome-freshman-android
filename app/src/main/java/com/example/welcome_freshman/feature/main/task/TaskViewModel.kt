package com.example.welcome_freshman.feature.main.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.data.model.Task
import com.example.welcome_freshman.data.repository.TaskRepository
import com.example.welcome_freshman.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 *@date 2024/3/11 10:57
 *@author GFCoder
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _taskUiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)

    val taskUiState = _taskUiState

    init {
        getTasks()
    }

    fun getTasks() {
        viewModelScope.launch {
            try {
                val tasks = taskRepository.getTasksByUserId(userRepository.userData.first().userId)
                if (tasks.isNotEmpty()) {
                    _taskUiState.value = TaskUiState.Success(tasks)
                }
            } catch (e: IOException) {
                _taskUiState.value = TaskUiState.Error
                Log.e("task request", "task request fail: ", e)
            }
        }
    }
}

sealed interface TaskUiState {
    data class Success(val tasks: List<Task>) : TaskUiState
    object Error : TaskUiState
    object Loading : TaskUiState
}