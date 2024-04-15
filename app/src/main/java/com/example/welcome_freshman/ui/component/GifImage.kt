package com.example.welcome_freshman.ui.component

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

/**
 *@date 2024/4/10 21:46
 *@author GFCoder
 */

@Composable
fun GifImage(
    @DrawableRes painter: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = painter,
            imageLoader = imageLoader
        ),
        contentDescription = null, modifier = modifier
    )
}

