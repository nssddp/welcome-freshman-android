package com.example.welcome_freshman.feature.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.data.model.User
import com.example.welcome_freshman.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/1/29 11:57
 *@author GFCoder
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    fun getUserById(id: String) {
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
    data class Success(val user: User) : ProfileUiState
    object Error : ProfileUiState
    object Loading : ProfileUiState
}