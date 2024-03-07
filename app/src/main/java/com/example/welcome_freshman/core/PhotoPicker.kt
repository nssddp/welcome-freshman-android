package com.example.welcome_freshman.core

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

/**
 *@date 2024/3/6 21:00
 *@author GFCoder
 */
@Composable
fun rememberPhotoPicker(onImagePicked: (Uri?) -> Unit): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {

                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
            onImagePicked(uri)
        }
    )
    return pickMedia
}