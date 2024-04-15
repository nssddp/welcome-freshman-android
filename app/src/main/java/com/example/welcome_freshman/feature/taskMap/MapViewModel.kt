package com.example.welcome_freshman.feature.taskMap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MyLocationData
import com.example.welcome_freshman.core.BDLocationManager
import com.example.welcome_freshman.core.data.model.SubTask
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.repository.TaskRepository
import com.example.welcome_freshman.feature.certification.CAMERA_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@date 2024/4/5 15:06
 *@author GFCoder
 */


@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationManager: BDLocationManager,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    val subTaskDetailArg: String = checkNotNull(savedStateHandle[SUB_TASK_ID_TO_MAP_ARG])
    val cameraIdArg: String = checkNotNull(savedStateHandle[CAMERA_ID_ARG])

    private val _locationData = MutableStateFlow<MyLocationData?>(null)
    val locationData = _locationData

    private val _taskMapUiState = MutableStateFlow<TaskMapUiState>(TaskMapUiState.Loading)
    val taskMapUiState = _taskMapUiState

    init {
//        getMapLocation()
        getTaskInfo()
    }

    fun getMapLocation(map: BaiduMap) {
        locationManager.getMapLocation(map)
    }

    fun getTaskInfo() {
        viewModelScope.launch {

            try {
                val tasks = taskRepository.getRegionTask()
                if (tasks != null) {
                    _taskMapUiState.value = TaskMapUiState.Success(tasks)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getSubTaskInfo(){

    }



}


sealed interface TaskMapUiState {
    data class Success(val taskCoordinate: List<SubTask>) : TaskMapUiState
    object Error : TaskMapUiState
    object Loading : TaskMapUiState
}