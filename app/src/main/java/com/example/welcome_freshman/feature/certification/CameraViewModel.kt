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
import kotlinx.coroutines.flow.MutableStateFlow
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
    fun takePhoto(portrait: Bitmap) {
        viewModelScope.launch {
            try {
                val portraitBase64 = portrait.toBase64()
                userRepository.checkPortrait(portraitBase64)
            } catch (exc: Exception) {
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


fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val planeProxy = image.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.toBase64(): String {
    ByteArrayOutputStream().apply {
        this@toBase64.compress(Bitmap.CompressFormat.JPEG, 100, this)
        val byteArray = this.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}