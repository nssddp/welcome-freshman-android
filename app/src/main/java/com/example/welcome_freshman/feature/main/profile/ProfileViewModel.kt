package com.example.welcome_freshman.feature.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.repository.AdRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/1/29 11:57
 *@author GFCoder
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val adRepository: AdRepository,
) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    private val userPref = userRepository.userData

    private val _user = MutableStateFlow<User?>(null)
    val user = _user

    init {
//        getUserInfo()
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
    /*fun getUserById(id: String) {
        viewModelScope.launch {
            userRepository.getUserById(id)
                .map { user ->
                    ProfileUiState.Success(user)
                }
                .catch {
                    _profileUiState.value = ProfileUiState.Error
                }
                .collect { uiState ->
                    _profileUiState.value = uiState
                }
        }
    }*/

    fun getUserInfo() {
        viewModelScope.launch {
            if (_profileUiState.value != ProfileUiState.Loading)
                _profileUiState.value = ProfileUiState.Loading
//            Log.d("loadingScreen","加载屏幕")
//            delay(1000)
            try {
                userPref.collect { pref ->
                    Log.d("学号", pref.toString())
                    val user = userRepository.getUserById(pref.userId)
                    if (user != null) {
                        if (user.avatarUrl.isNullOrBlank()) user.avatarUrl = null
                        Log.d("profileUserInfo", user.toString())
                        _user.value = user
                        _profileUiState.value = ProfileUiState.Success
                    }
                }
            } catch (e: Exception) {
                Log.e("profileUserInfo", "connect error......", e)
                _profileUiState.value = ProfileUiState.Error
            }

        }
    }


}

/*private suspend fun profileUiState(
    userId: String,
    userRepository: UserRepository
): Flow<ProfileUiState> {
    return userRepository.getUserById(userId).map (ProfileUiState::Success).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading
    )

}*/

sealed interface ProfileUiState {
    object Success : ProfileUiState
    object Error : ProfileUiState
    object Loading : ProfileUiState
}