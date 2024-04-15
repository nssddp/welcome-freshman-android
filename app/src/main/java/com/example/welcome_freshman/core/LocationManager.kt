package com.example.welcome_freshman.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/19 15:53
 *@author GFCoder
 */

/**
 * Composable function to request location permissions and handle different scenarios.
 *
 * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
 * @param onPermissionDenied Callback to be executed when any requested permission is denied.
 * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}


/**
 * Retrieves the current user location asynchronously.
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@Singleton
@SuppressLint("MissingPermission")
class TestLocationManager @Inject constructor(
    private val locationManager: LocationManager,
    /*onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,*/
//    priority: Boolean = true,
    @ApplicationContext private val context: Context,
) {
    // Determine the accuracy priority based on the 'priority' parameter
//    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
//    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateLocation(location)

            removeLocationListener()
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    fun startLocation(): Pair<Double, Double>? {
        if (context.areLocationPermissionsGranted()) {

            val location = getLastKnownLocation()
//            updateLocation(location)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0f,
                locationListener
            )
            if (location != null)
                return Pair(location.longitude, location.latitude)
        }
        return null
    }


    fun updateLocation(
        location: Location?,
//        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    ) {
        if (location != null) {
            Log.d("LatestLocation", location.toString())
//            onGetCurrentLocationSuccess(Pair(location.latitude, location.longitude))
        }

    }

    private fun getLastKnownLocation(): Location? {

        val providers = locationManager.getProviders(true)

        var bestLocation: Location? = null

        for (provider in providers) {
            @SuppressLint("MissingPermission")
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }

        return bestLocation
    }

    fun removeLocationListener() {
        locationManager.removeUpdates(locationListener)
    }

}

@Singleton
class BDLocationManager @Inject constructor(
    private val locationClient: LocationClient?,
    private val option: LocationClientOption,
    @ApplicationContext private val context: Context,
) {

    fun getLocation(onGetLocation: (Pair<Double, Double>) -> Unit) {
//        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.setLocationPurpose(LocationClientOption.BDLocationPurpose.SignIn)
//        option.setOnceLocation(true)

        if (locationClient != null && context.areLocationPermissionsGranted()) {
            locationClient.locOption = option

            val locationListener = object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    if (location != null) {
                        onGetLocation(Pair(location.longitude, location.latitude))
                        locationClient.stop()
                    }
                }
            }
            if (!locationClient.isStarted) {
                locationClient.registerLocationListener(locationListener)
                locationClient.start()
            }
        }
    }

    private var firstLoc = true
    fun getMapLocation(map: BaiduMap /*onGetLocData: (MyLocationData) -> Unit*/) {


        if (locationClient != null && context.areLocationPermissionsGranted()) {
            locationClient.locOption = option.apply {
                setCoorType("bd09ll")
            }

            val locationListener = object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    if (location != null) {
//                        locationClient.stop()

                        val locationData = MyLocationData.Builder()
//                            .accuracy(location.radius)
                            .direction(location.direction)
                            .longitude(location.longitude).latitude(location.latitude)
                            .build()
//                        if (firstLoc) {
                        firstLoc = false
                        map.setMapStatus(
                            MapStatusUpdateFactory.newMapStatus(
                                MapStatus.Builder()
                                    .zoom(19f)
                                    .target(LatLng(30.831086,106.125039))
                                    .build()
                            )
                        )
//                        }
                        map.setMyLocationData(locationData)
                        locationClient.stop()

                    }
                }
            }
            if (!locationClient.isStarted) {
                locationClient.registerLocationListener(locationListener)
                locationClient.start()
            } else {
                locationClient.unRegisterLocationListener(locationListener)
                locationClient.registerLocationListener(locationListener)
                locationClient.restart()
            }
        }
    }


}


/**
 * Checks if location permissions are granted.
 *
 * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
 */
fun Context.areLocationPermissionsGranted(): Boolean {
    return (ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}


@Module
@InstallIn(SingletonComponent::class)
class LocationModule {
    @Provides
    fun provideLocationManager(
        @ApplicationContext context: Context
    ): LocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager


    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context): LocationClient? {
        // 配置 LocationClient，例如设置定位模式、坐标类型等
        // ... 其他配置
        try {
            return LocationClient(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @Provides
    @Singleton
    fun provideLocationOption(): LocationClientOption = LocationClientOption()

}


