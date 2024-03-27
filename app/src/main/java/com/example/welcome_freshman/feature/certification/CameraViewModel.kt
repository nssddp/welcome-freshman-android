package com.example.welcome_freshman.feature.certification

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.welcome_freshman.core.BDLocationManager
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
    private val userRepository: UserRepository,
    private val locationManager: BDLocationManager,
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : ViewModel() {
    private var _cameraUiState = MutableStateFlow<CameraUiState>(CameraUiState.WaitValid)

    val cameraUiState = _cameraUiState

    private var _coordinate = MutableStateFlow<Pair<Double, Double>?>(null)

    fun takePhoto(portrait: Bitmap) {
        _cameraUiState.value = CameraUiState.Loading
        locationManager.getLocation { location ->
            viewModelScope.launch {

                try {
                    val portraitBase64 = bitmap2Base64(portrait)
                    val userId = userRepository.userData.first().userId
                    val respCode =
                        userRepository.checkPortrait(portraitBase64, userId, location)
//                Log.d("ValidCode", respCode.toString())

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
    }


    /*@SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(
        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetCurrentLocationFailed: (Exception) -> Unit,
        priority: Boolean = true,
        context: Context,
    ) {

        var location: Location?
        if (context.areLocationPermissionsGranted()) {




            while (_coordinate.value == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                Log.d("LatestLocation", _coordinate.value.toString())
                location?.let { onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude)) }


                if (location != null) locationManager.removeUpdates(locationListener)
            }

        }
    }*/


}

sealed interface CameraUiState {
    object WaitValid : CameraUiState

    object Success : CameraUiState
    object Error : CameraUiState
    object Loading : CameraUiState
}


/*fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val planeProxy = image.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}*/




fun bitmap2Base64(bitmap: Bitmap): String {
    ByteArrayOutputStream().apply {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
        val byteArray = this.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}