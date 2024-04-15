package com.example.welcome_freshman.feature.certification

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.ui.component.ValidCircularIndicator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@date 2024/1/29 18:06
 *@author GFCoder
 */


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraRoute(viewModel: CameraViewModel = hiltViewModel(), onValidSuccess: () -> Unit) {
//    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val multiPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(multiPermissionState) {
//        cameraPermissionState.launchPermissionRequest()
        multiPermissionState.launchMultiplePermissionRequest()
    }


    val uiState by viewModel.cameraUiState.collectAsState()

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }/*.apply { cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA }*/
    }

    if (multiPermissionState.allPermissionsGranted) {
        when (viewModel.cameraIdArg) {
            "1" -> CameraScreen(
                onPhotoTaken = { viewModel.takePhoto(it) },
                validState = uiState,
                onValidSuccess = onValidSuccess,
                controller = controller
            )

            "2" -> IdCardScreen(
                onPhotoTaken = { viewModel.takePhoto(it) },
                validState = uiState,
                onValidSuccess = onValidSuccess,
                controller = controller
            )

            "3" -> ItemCameraScreen(
                onPhotoTaken = { viewModel.takePhoto(it) },
                validState = uiState,
                onValidSuccess = onValidSuccess,
                controller = controller
            )
            "4" -> ItemCameraScreen(
                onPhotoTaken = { viewModel.takePhoto(it) },
                validState = uiState,
                onValidSuccess = onValidSuccess,
                controller = controller
            )
        }
    } else {
        Text("需要相机权限", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}

@Composable
fun CameraScreen(
    onPhotoTaken: (Bitmap) -> Unit,
    validState: CameraUiState,
    onValidSuccess: () -> Unit,
    controller: LifecycleCameraController,
) {

    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }

    val context = LocalContext.current

    controller.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
//                .padding(it)
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            Modifier
                .size(235.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(4.dp, rainbowColorsBrush),
                    CircleShape
                ),
            controller
        )

        /*IconButton(
            modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
                .size(66.dp),
            onClick = {
                scope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
        ) {
            Icon(
                imageVector = Icons.Default.BrowseGallery,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(46.dp)
            )
        }*/
        var validIsEnabled by remember {
            mutableStateOf(true)
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .size(66.dp),
            onClick = {
                takePhoto(
                    context = context,
                    controller = controller,
                    onPhotoTaken = onPhotoTaken
                )

            },
            enabled = validIsEnabled,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.take_photo_icon),
                contentDescription = null,
                modifier = Modifier.size(66.dp)
            )
        }

        AnimatedContent(
            validState,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(500)
                ) togetherWith fadeOut(animationSpec = tween(500))
            },

            label = "Animated Content"
        ) { targetState ->
            when (targetState) {
                CameraUiState.Loading -> {
                    ValidCircularIndicator(indicatorState = 0)
                    validIsEnabled = false
                }

                CameraUiState.Success -> {
                    ValidCircularIndicator(indicatorState = 1)
                    LaunchedEffect(Unit) {
                        scope.launch {
                            delay(1000)
                            onValidSuccess()
                        }
                    }

                }

                CameraUiState.Error -> {
                    ValidCircularIndicator(indicatorState = 2)
                    validIsEnabled = true
                }

                CameraUiState.WaitValid -> {
                    validIsEnabled = true
                }

            }
        }

    }


}

@Composable
fun IdCardScreen(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    validState: CameraUiState,
    onValidSuccess: () -> Unit,
) {
    controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            controller = controller
        )
//        Modifier.size(width = 322.dp, height = 200.dp).clip(RoundedCornerShape(10)),
        // 使用LocalDensity来获取当前的density
        val density = LocalDensity.current

        // 将dp值转换为px
        val rectWidthPx = with(density) { 265.dp.toPx() }
        val rectHeightPx = with(density) { 426.dp.toPx() }
        val cornerRadiusPx = with(density) { 20.dp.toPx() } // 圆角的半径


        Canvas(modifier = Modifier.fillMaxSize()) {
            val left = (size.width - rectWidthPx) / 2
            val top = (size.height - rectHeightPx) / 2
            val right = left + rectWidthPx
            val bottom = top + rectHeightPx

            val path = Path().apply {
                // 添加一个大的矩形路径，覆盖整个Canvas
                // 创建一个圆角矩形路径
                addRoundRect(
                    RoundRect(
                        left = left,
                        top = top,
                        right = right,
                        bottom = bottom,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )

                )
            }

            // 使用clipPath对路径进行裁剪，但这里我们填充背景色以实现相反的效果
            clipPath(path, clipOp = ClipOp.Difference) {
                drawRect(
                    color = Color.Black.copy(.8f),
                    size = size
                )
            }

            drawRoundRect(
                color = Color.Blue,
                topLeft = Offset(left, top),
                size = Size(rectWidthPx, rectHeightPx),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                style = Stroke(3f)
            )
        }


        // Draw the overlay with a hole for the image
        /*Canvas(modifier = Modifier.fillMaxSize()) {
            val paint =  Color.Black.copy(alpha = 0.8f)

            clipRect {
                // Draw the overlay outside the image area
                drawRect(color = paint)
                // Assuming the hole for the image should be transparent
                drawRect(
                    color = Color.Transparent,
                    topLeft = center.copy(x = center.x - 300.dp.toPx(), y = center.y - 300.dp.toPx()),
                    size = size.copy(width = 600.dp.toPx(), height = 600.dp.toPx())
                )
            }
        }*/

        Text(
            text = "请将身份证放入框内, 点击按钮上传识别",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp),
            color = Color.White
        )

        var validIsEnabled by remember {
            mutableStateOf(true)
        }
        IconButton(
            onClick = {
                takePhoto(
                    context = context,
                    controller = controller,
                    onPhotoTaken = onPhotoTaken
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 46.dp)
                .size(66.dp),
            enabled = validIsEnabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.take_photo_icon),
                contentDescription = null,
                modifier = Modifier.size(66.dp)
            )
        }

        AnimatedContent(
            validState,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(500)
                ) togetherWith fadeOut(animationSpec = tween(500))
            },

            label = "Animated Content"
        ) { targetState ->
            when (targetState) {
                CameraUiState.Loading -> {
                    ValidCircularIndicator(indicatorState = 0)
                    validIsEnabled = false
                }

                CameraUiState.Success -> {
                    ValidCircularIndicator(indicatorState = 1)
                    LaunchedEffect(Unit) {
                        scope.launch {
                            delay(1000)
                            onValidSuccess()
                        }
                    }

                }

                CameraUiState.Error -> {
                    ValidCircularIndicator(indicatorState = 2)
                    validIsEnabled = true
                }

                CameraUiState.WaitValid -> {
                    validIsEnabled = true
                }

            }
        }

    }
}


@Composable
fun ItemCameraScreen(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    validState: CameraUiState,
    onValidSuccess: () -> Unit,
) {
    controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            controller = controller
        )

        IconButton(
            onClick = {
                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    controller.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                else controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 30.dp, end = 20.dp)
        ) {
            Icon(imageVector = Icons.Filled.Cameraswitch, contentDescription = null)
        }

        var validIsEnabled by remember {
            mutableStateOf(true)
        }
        IconButton(
            onClick = {
                takePhoto(
                    context = context,
                    controller = controller,
                    onPhotoTaken = onPhotoTaken
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 46.dp)
                .size(66.dp),
            enabled = validIsEnabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.take_photo_icon),
                contentDescription = null,
                modifier = Modifier.size(66.dp)
            )
        }

        AnimatedContent(
            validState,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(500)
                ) togetherWith fadeOut(animationSpec = tween(500))
            },

            label = "Animated Content"
        ) { targetState ->
            when (targetState) {
                CameraUiState.Loading -> {
                    ValidCircularIndicator(indicatorState = 0)
                    validIsEnabled = false
                }

                CameraUiState.Success -> {
                    ValidCircularIndicator(indicatorState = 1)
                    LaunchedEffect(Unit) {
                        scope.launch {
                            delay(1000)
                            onValidSuccess()
                        }
                    }

                }

                CameraUiState.Error -> {
                    ValidCircularIndicator(indicatorState = 2)
                    validIsEnabled = true
                }

                CameraUiState.WaitValid -> {
                    validIsEnabled = true
                }

            }
        }
    }


}

@Composable
fun CameraPreview(modifier: Modifier = Modifier, controller: LifecycleCameraController) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)

            }
        },
        modifier = modifier
    )

}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                onPhotoTaken(image.toBitmap())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("CameraCapture", "Couldn't take photo: ", exception)
            }
        }
    )

}

@Composable
fun PhotoBottomSheet(
    bitmaps: List<Bitmap>,
    modifier: Modifier = Modifier
) {
    if (bitmaps.isEmpty()) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "there are no photos yet")
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ) {
            items(bitmaps) { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.clip(
                        RoundedCornerShape(10.dp)
                    )
                )
            }
        }

    }
}

/*factory = { ctx ->
    val previewView = PreviewView(ctx).apply {
        this.controller = controller
        controller.bindToLifecycle(lifecycleOwner)
    }
    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
    val executor = ContextCompat.getMainExecutor(ctx)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // 预览
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // 拍照
        val imageCapture = ImageCapture.Builder().build()
            .also {

            }


        // 分析
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(executor, ImageAnalysis.Analyzer { image ->
                    Log.d("imageInfo", image.imageInfo.toString())

                    //  解析成功，则关闭imageProxy
                    image.close()
                })

            }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageAnalysis, imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraPreview", "绑定失败", exc)
        }
    }, executor)

    previewView
}*/





