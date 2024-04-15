package com.example.welcome_freshman.feature.taskMap

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BaiduMapOptions
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.model.LatLng
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.SubTask
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 *@date 2024/4/4 22:26
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TaskMapRoute(
    onCheckIn: (String, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
//    val locationData by viewModel.locationData.collectAsState()
    val multiPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val uiState by viewModel.taskMapUiState.collectAsState()

    LaunchedEffect(multiPermissionState) {
//        cameraPermissionState.launchPermissionRequest()
        multiPermissionState.launchMultiplePermissionRequest()
    }
    if (multiPermissionState.allPermissionsGranted) {
        TaskMapScreen(
            uiState = uiState,
            getLocationData = { viewModel.getMapLocation(it) },
            onBackClick = onBackClick,
            onCheckIn = { onCheckIn(viewModel.cameraIdArg, viewModel.subTaskDetailArg) },
            cameraId = viewModel.cameraIdArg
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskMapScreen(
    getLocationData: (map: BaiduMap) -> Unit,
    onBackClick: () -> Unit,
    onCheckIn: () -> Unit,
    uiState: TaskMapUiState,
    cameraId: String
) {
    val scaffoldState = rememberBottomSheetScaffoldState()


    BottomSheetScaffold(
        sheetContent = {
           /* Box(
                modifier = Modifier
//                .padding(it)
                    .fillMaxSize()
            ) {

            }*/
        },
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = {},
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            if (uiState is TaskMapUiState.Success)
                BDMap(
                    getLocationData = getLocationData,
                    tasksCoordinate = taskCoordinate2LatLng(uiState.taskCoordinate)
                )


            TopAppBar(
                title = { Text(text = "任务地图") },
//                modifier = Modifier.background(Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            if (cameraId != "0")
                FloatingActionButton(
                    onClick = { onCheckIn() },
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = "打卡")
                }
        }

    }


}

@Composable
fun BDMap(
    tasksCoordinate: List<TaskCoordinateInfo>,
    bdMapOptionsFactory: () -> BaiduMapOptions = { BaiduMapOptions() },
    getLocationData: (map: BaiduMap) -> Unit,
) {
    val context = LocalContext.current

    val taskPoints =
        mutableListOf(LatLng(30.833101, 106.124616), LatLng(30.830543, 106.122811))

    val markIcon = BitmapDescriptorFactory.fromResource(R.drawable.bd_marker)
    val options = mutableListOf<OverlayOptions>()
    tasksCoordinate.fastForEach {
        val mBundle = Bundle()
        mBundle.putString("title", it.taskName)
        val option: OverlayOptions = MarkerOptions()
            .position(it.latLng)
            .icon(markIcon)
            .extraInfo(mBundle)
            .perspective(true)
            .clickable(true)

        options.add(option)
    }


    val mapView = MapView(context, bdMapOptionsFactory()).apply {
//        removeViewAt(1)
        map.isMyLocationEnabled = true
        map.addOverlays(options)

    }
    val map = mapView.map
    getLocationData(map)

    map.setOnMarkerClickListener { mark ->
        mark.extraInfo.getString("title")?.let { context.toastTask(it) }
        false
    }


    AndroidView(
//        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        onRelease = {
            runCatching { it.onDestroy() }
            it.removeAllViews()
        }
    )

    MapLifecycle(mapView = mapView)

}

private fun Context.toastTask(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

private fun taskCoordinate2LatLng(tasks: List<SubTask>): List<TaskCoordinateInfo> {
    val latLngList = mutableListOf<TaskCoordinateInfo>()

    tasks.fastForEach {
        if (it.centerLatitude != null && it.centerLongitude != null)
            latLngList.add(
                TaskCoordinateInfo(
//                    taskId = it.subTaskId!!,
//                    taskName = it.subTaskName!!,
                    latLng = LatLng(it.centerLatitude, it.centerLongitude)
                )
            )
    }


    return latLngList
}

data class TaskCoordinateInfo(
    val taskId: Int? = null,
    val taskName: String? = null,
    val latLng: LatLng,

    )


@Composable
private fun MapLifecycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(previousState)

        lifecycle.addObserver(mapLifecycleObserver)

        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            // fix memory leak
            runCatching { mapView.onDestroy() }
            mapView.removeAllViews()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskMapScreenPreview() {
//    TaskMapScreen()
}

private fun MapView.lifecycleObserver(previousState: MutableState<Lifecycle.Event>): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                // Skip calling mapView.onCreate if the lifecycle did not go through onDestroy - in
                // this case the BDMap composable also doesn't leave the composition. So,
                // recreating the map does not restore state properly which must be avoided.
                if (previousState.value != Lifecycle.Event.ON_STOP) {
                    this.onCreate(context, Bundle())
                }
            }

            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            else -> { /* ignore */
            }
        }
        previousState.value = event
    }