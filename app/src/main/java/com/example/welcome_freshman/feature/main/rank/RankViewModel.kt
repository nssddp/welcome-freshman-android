package com.example.welcome_freshman.feature.main.rank

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.model.SingleTaskRank
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/3/13 15:26
 *@author GFCoder
 */


@HiltViewModel
class RankViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RankUiState>(RankUiState.Loading)
    val uiState = _uiState

    /*init {
        getRanks()
    }*/

    fun getRanks(id: Int) {
        _uiState.value = RankUiState.Loading

        viewModelScope.launch {
            try {
                if (id == 2) {
                    val ranks = userRepository.getRankShort()
                    if (ranks != null) {
                        _uiState.value =
                            RankUiState.Success(RankDataType(ranks = null, rankShort = ranks))
                    }
                } else {
                    val ranks = userRepository.getRanks(id)
                    if (ranks != null) {
                        _uiState.value =
                            RankUiState.Success(RankDataType(ranks = ranks, rankShort = null))
                    }
                }

            } catch (e: Exception) {
                Log.e("rank request", "rank request fail", e)
                _uiState.value = RankUiState.Error
            }
        }
    }

}

sealed interface RankUiState {
    class Success(val rankData: RankDataType) : RankUiState
    object Error : RankUiState
    object Loading : RankUiState
}

data class RankDataType(
    val ranks: List<User>?,
    val rankShort: List<SingleTaskRank>?
)