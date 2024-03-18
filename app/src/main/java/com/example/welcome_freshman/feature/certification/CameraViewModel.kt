package com.example.welcome_freshman.feature.certification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 *@date 2024/3/10 17:09
 *@author GFCoder
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _cameraUiState = MutableStateFlow<CameraUiState>(CameraUiState.WaitValid)

    val cameraUiState = _cameraUiState

    fun takePhoto(portrait: Bitmap) {
        viewModelScope.launch {
            _cameraUiState.value = CameraUiState.Loading
            try {
                val portraitBase64 = bitmap2Base64(portrait)
                val userId = userRepository.userData.first().userId
                val respCode = userRepository.checkPortrait(portraitBase64, userId)
                Log.d("ValidCode", respCode.toString())

                if (respCode == 200) {
                    _cameraUiState.value = CameraUiState.Success
                    userRepository.setValidState(true)
                    Log.d("faceValid", "faceValid success................")
                } else if (respCode == 0) {
                    _cameraUiState.value = CameraUiState.Error
                    delay(1000)
                    _cameraUiState.value = CameraUiState.WaitValid

                }
            } catch (exc: Exception) {
                _cameraUiState.value = CameraUiState.Error
                delay(1000)
                _cameraUiState.value = CameraUiState.WaitValid
                Log.e("checkPortrait", "checkPortrait fail: ", exc)
            }
        }
    }

    private val _bitmap = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmap = _bitmap
    fun viewTakenPhoto(bitmap: Bitmap) {
        _bitmap.value += bitmap

    }

}

sealed interface CameraUiState {
    object WaitValid : CameraUiState

    object Success : CameraUiState
    object Error : CameraUiState
    object Loading : CameraUiState
}


fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val planeProxy = image.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun bitmap2Base64(bitmap: Bitmap): String {
    ByteArrayOutputStream().apply {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
        val byteArray = this.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}