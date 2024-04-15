package com.example.welcome_freshman

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.common.BaiduMapSDKException
import dagger.hilt.android.HiltAndroidApp


/**
 *@date 2024/1/29 11:59
 *@author GFCoder
 */

@HiltAndroidApp
class WfApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SDKInitializer.setCoordType(CoordType.BD09LL)
        SDKInitializer.setAgreePrivacy(this, true)
        try {
            // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
            SDKInitializer.initialize(this)
        } catch (e: BaiduMapSDKException) {
            e.printStackTrace()
        }

    }
}