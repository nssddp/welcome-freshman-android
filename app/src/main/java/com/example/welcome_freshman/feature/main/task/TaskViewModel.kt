package com.example.welcome_freshman.feature.main.task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.HomeDto
import com.example.welcome_freshman.core.data.repository.AdRepository
import com.example.welcome_freshman.core.data.repository.TaskRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/3/11 10:57
 *@author GFCoder
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val adRepository: AdRepository,
) : ViewModel() {
    private val _taskUiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
//    val shouldShowDialogAD:Boolean = checkNotNull(savedStateHandle[IS_COMPLETE_TASK])

    val taskUiState = _taskUiState

    init {
        getTasks()
        getAdUrl()
    }

    private val _adUrl  = MutableStateFlow<String?>("")
    val adUrl = _adUrl

    private fun getAdUrl() {
        viewModelScope.launch {
            try {

                val adUrl = adRepository.getAdNoLogin()
                if (!adUrl.isNullOrBlank())
                    _adUrl.value = adUrl
            } catch (e: Exception) {
                e.printStackTrace()

            }

        }
    }

    fun getTasks() {
        viewModelScope.launch {
            try {
                _taskUiState.value = TaskUiState.Loading
                val homeDto = taskRepository.getTasksByUserId(userRepository.userData.first().userId)
                if (homeDto != null) {
                    _taskUiState.value = TaskUiState.Success(homeDto)
                }
            } catch (e: Exception) {
                _taskUiState.value = TaskUiState.Error
                Log.e("task request", "task request fail: ", e)
            }
        }
    }
}

sealed interface TaskUiState {
    data class Success(val homeDto: HomeDto) : TaskUiState
    object Error : TaskUiState
    object Loading : TaskUiState
}