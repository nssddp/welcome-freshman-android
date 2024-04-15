package com.example.welcome_freshman.core.data.network.di

import androidx.compose.ui.util.trace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *@date 2024/3/7 16:59
 *@author GFCoder
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
//        isLenient = true
    }

    private val requestInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header(
                "Connection", "close"
            ) // 设置请求头
            .build()
        chain.proceed(modifiedRequest)
    }
    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("WfOkHttpClient") {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(requestInterceptor)
            .build()
    }
}