package com.example.welcome_freshman.feature.quiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.QuizDto
import com.example.welcome_freshman.core.data.repository.AdRepository
import com.example.welcome_freshman.core.data.repository.TaskRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/4/8 20:37
 *@author GFCoder
 */


@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val adRepository: AdRepository,
) : ViewModel() {
    private val _quizUiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)

    val quizUiState = _quizUiState

    private val subTaskArg: Int = checkNotNull(savedStateHandle[SUB_TASK_TO_QUIZ_ID_ARG])

    init {
        getQuizList()
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
    fun getQuizList() {
        viewModelScope.launch {
            _quizUiState.value = QuizUiState.Loading
            try {
                val quizList = taskRepository.getQuizList(subTaskArg)
                if (quizList != null)
                    _quizUiState.value = QuizUiState.Success(quizList)

            } catch (e: Exception) {
                Log.e("quiz request", "request fail", e)
                e.printStackTrace()
            }
        }
    }

    fun QuizCompleted(res: Boolean) {
        viewModelScope.launch {
            try {
                val userId = userRepository.userData.first().userId
                taskRepository.quizCompleted(userId, subTaskArg, res)
            } catch (e: Exception) {
                Log.e("commit quiz res", "commit fail: ", e)
            }
        }
    }


}

sealed interface QuizUiState {
    object Loading : QuizUiState
    data class Success(val quizList: List<QuizDto>) : QuizUiState

    object Error: QuizUiState
}
