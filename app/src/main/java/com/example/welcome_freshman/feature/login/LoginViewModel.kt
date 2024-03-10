package com.example.welcome_freshman.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.welcome_freshman.data.model.LoginRequest
import com.example.welcome_freshman.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

/**
 *@date 2024/3/8 20:29
 *@author GFCoder
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun doLogin(stuId: Int, pwd: String): Boolean {
        try {
            val resp = userRepository.loginCheck(LoginRequest(stuId, pwd))
            if (resp.code == 200) {
                userRepository.setUserId(stuId)
                return true
            }
        }catch (e: IOException){
            Log.d("LoginMsg","connect error......")
        }
        userRepository.setUserId(stuId)

        return true
    }

    suspend fun logout() {
        userRepository.setUserId(0)
    }

}

