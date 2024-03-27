package com.example.welcome_freshman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.UserData
import com.example.welcome_freshman.core.data.repository.AdRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/3/9 21:15
 *@author GFCoder
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val adRepository: AdRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userRepository.userData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _adUrl  = MutableStateFlow<String?>("")
    val adUrl = _adUrl

    init {
        getAdUrl()
    }

    private fun getAdUrl() {
        viewModelScope.launch {
            try {
                _adUrl.value = adRepository.getAd(userRepository.userData.first().userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}