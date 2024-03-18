package com.example.welcome_freshman.feature.updateUserInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/3/16 19:21
 *@author GFCoder
 */


@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val userRepository: UserRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow<UpdateUiState>(UpdateUiState.Loading)
    val uiState = _uiState

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri = _avatarUri

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            try {
                val userId = userRepository.userData.first().userId

                val userData = userRepository.getUserById(userId)
                if (userData != null) {
                    if (userData.avatarUrl.isNullOrBlank()) userData.avatarUrl = null
                    _uiState.value = UpdateUiState.Success(userData)
                }

            } catch (e: Exception) {
                Log.e("getUserInfo", "getUserInfo fail: ", e)
            }
        }


    }

    suspend fun updateUserInfo(user: User) {

        Log.d("userInfo", user.toString())
        userRepository.updateUser(user)
    }

    fun uploadAvatar(avatarData: ByteArray?, userId: Int) {

        viewModelScope.launch {
            try {
                if (avatarData != null) {
                    val uri = userRepository.uploadAvatar(avatarData, userId)

                    if (!uri.isNullOrBlank()) {
                        Log.d("avatarUri", uri)
                        _avatarUri.value = uri
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}

sealed interface UpdateUiState {
    data class Success(val user: User) : UpdateUiState
    object Error : UpdateUiState
    object Loading : UpdateUiState
}