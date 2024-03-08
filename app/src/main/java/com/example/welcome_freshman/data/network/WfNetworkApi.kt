package com.example.welcome_freshman.data.network

import com.example.welcome_freshman.data.model.LoginRequest
import com.example.welcome_freshman.data.model.Task
import com.example.welcome_freshman.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/6 19:03
 *@author GFCoder
 */

private interface WfNetworkApi {
    @GET("user/{id}")
    suspend fun getUserById(@Path("id") id: String?): NetworkResponse<User>

    @POST("user/users/stuLogin")
    suspend fun loginCheck(@Body loginRequest: LoginRequest): NetworkResponse<User>

    @GET("task")
    suspend fun getTaskById(@Query("id") id: String?): NetworkResponse<Task>

}

interface WfNetworkDataSource {
    suspend fun getUserById(id: String? = null): User

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>


    suspend fun getTaskById(id: String? = null): Task

}

@Serializable
data class NetworkResponse<T>(
    val code: Int, // 状态码
    val message: String, // 消息
    val data: T,
    val token: String, // 登录成功后返回的token

)

private const val WF_BASE_URL = "http://localhost:11000"

@Singleton
class RetrofitWfNetwork @Inject constructor(
    networkJson: Json
) : WfNetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(WF_BASE_URL)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(WfNetworkApi::class.java)

    override suspend fun getUserById(id: String?): User = networkApi.getUserById(id).data
    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> = networkApi.loginCheck(loginRequest)

    override suspend fun getTaskById(id: String?): Task = networkApi.getTaskById(id).data
}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsNetworkDataSource(impl: RetrofitWfNetwork): WfNetworkDataSource
}
