package com.example.welcome_freshman.feature.comment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.Comment
import com.example.welcome_freshman.core.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/4/3 16:11
 *@author GFCoder
 */


@HiltViewModel
class CommentViewModel @Inject constructor(
    private val taskRepository: TaskRepository

) : ViewModel() {
    private val _commentUiState = MutableStateFlow<CommentUiState>(CommentUiState.Loading)

    val commentUiState = _commentUiState

    fun getComment(taskId: Int) {
        viewModelScope.launch {
            _commentUiState.value = CommentUiState.Loading
            try {
                val comments = taskRepository.getCommentByTaskId(taskId)
                if (comments != null)
                    _commentUiState.value = CommentUiState.Success(comments)
            } catch (e: Exception) {
                Log.e("Comment request", "request fail: ", e)
            }

        }
    }


}

sealed interface CommentUiState {
    data class Success(val comments: List<Comment>) : CommentUiState
    object Error : CommentUiState
    object Loading : CommentUiState
}




