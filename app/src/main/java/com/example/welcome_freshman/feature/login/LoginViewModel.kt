package com.example.welcome_freshman.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 *@date 2024/3/8 20:29
 *@author GFCoder
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Success)
    val uiState = _uiState
    suspend fun doLogin(stuId: Int, pwd: String): Pair<Boolean,String> {
        try {
            _uiState.value = LoginUiState.Loading
            val resp = userRepository.loginCheck(LoginRequest(stuId, pwd))
            if (resp.msg == "success") {
                _uiState.value = LoginUiState.Success
                userRepository.setUserId(stuId)
                return Pair(true,"")
            }
        } catch (e: Exception) {
            _uiState.value = LoginUiState.Error
            Log.e("LoginMsg", "connect error......", e)
//            return Pair(false,"请求超时")
        }

        return Pair(true,"账号或密码输入错误，请重试!")
    }

    suspend fun logout() {
        userRepository.setUserId(0)
    }

}

sealed interface LoginUiState {
    object Success : LoginUiState
    object Error : LoginUiState
    object Loading : LoginUiState
}
