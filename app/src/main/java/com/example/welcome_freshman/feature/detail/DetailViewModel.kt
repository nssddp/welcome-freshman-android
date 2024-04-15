package com.example.welcome_freshman.feature.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.Comment
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.repository.TaskRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/3/11 18:37
 *@author GFCoder
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val taskDetailArg: String = checkNotNull(savedStateHandle[TASK_ID_ARG])

    private val _taskDetailUiState = MutableStateFlow<TaskDetailUiState>(TaskDetailUiState.Loading)
    val taskDetailUiState = _taskDetailUiState

    init {
        getTaskDetail()
    }

    fun getTaskDetail() {
        viewModelScope.launch {
            Log.d("taskDetailArg", taskDetailArg)

            try {
                _taskDetailUiState.value = TaskDetailUiState.Loading

                val taskDetail = taskRepository.getTaskDetail(
                    userRepository.userData.first().userId,
                    taskDetailArg.toInt(),

                    )
                if (taskDetail != null) _taskDetailUiState.value =
                    TaskDetailUiState.Success(taskDetail)
            } catch (e: Exception) {
                Log.e("taskDetail request", "fail: ", e)
//                e.printStackTrace()
            }
        }

    }


    fun doComment(taskId: Int, content: String) {
        viewModelScope.launch {
            val userId = userRepository.userData.first().userId

            try {
                userRepository.doComment(
                    Comment(
                        userId = userId,
                        taskId = taskId,
                        content = content
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}

sealed interface TaskDetailUiState {
    data class Success(val taskDetail: Task) : TaskDetailUiState
    object Error : TaskDetailUiState
    object Loading : TaskDetailUiState
}
