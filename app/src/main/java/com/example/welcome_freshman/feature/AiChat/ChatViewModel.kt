package com.example.welcome_freshman.feature.AiChat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/4/11 22:16
 *@author GFCoder
 */


@HiltViewModel
class ChatViewModel @Inject constructor(
    val taskRepository: TaskRepository
) : ViewModel() {
    private val _respText = MutableStateFlow("")
    val respText = _respText


    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> get() = _messages

    fun sendMessage(message: ChatMessage) {
        _messages.value += message
    }

    fun chatReq(text: String) {
        viewModelScope.launch {

            try {
                val resp = taskRepository.chatReq(
                    mapOf(
                        "ques" to text,
                        "appKey" to "6614efe89ebcf2d2145fba46",
                        "uid" to "9I3eKM1712648168962UxJpwFoncV"
                    )
                            /*ChatModel(
                        ques = text,
                        appKey = "6614efe89ebcf2d2145fba46",
                        uid = "9I3eKM1712648168962UxJpwFoncV",
                        isLongChat = 0
                    )*/
                )
                if (!resp.isNullOrEmpty()) {
                    sendMessage(ChatMessage(text = resp, isSender = false))
                    _respText.value = resp
                }
            } catch (e: Exception) {
                Log.e("aichat", "aichat request fail: ", e)
            }


        }

    }

}