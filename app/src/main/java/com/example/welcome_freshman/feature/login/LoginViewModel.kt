package com.example.welcome_freshman.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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
    suspend fun doLogin(stuId: Int, pwd: String): Pair<Boolean, String> {
        _uiState.value = LoginUiState.Loading
        try {
            val resp = userRepository.loginCheck(LoginRequest(stuId, pwd))
            if (resp.msg == "success" && resp.data?.userId != null) {
                _uiState.value = LoginUiState.Success

                userRepository.setUserId(resp.data.userId)
                if (resp.data.validState == "已激活") userRepository.setValidState(true)
                else userRepository.setValidState(false)

                return Pair(true, "")
            }
        } catch (e: Exception) {
            _uiState.value = LoginUiState.Error
            Log.e("LoginMsg", "connect error......", e)
            return Pair(false, "请求超时")
        }
        _uiState.value = LoginUiState.Error
        return Pair(false, "账号或密码输入错误，请重试!")
    }

    suspend fun checkIsValid(): Boolean =
        userRepository.userData.first().validState


    suspend fun logout() {
        userRepository.setUserId(0)
    }

}

sealed interface LoginUiState {
    object Success : LoginUiState
    object Error : LoginUiState
    object Loading : LoginUiState
}
