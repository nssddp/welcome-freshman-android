package com.example.welcome_freshman.core.data.network

import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.model.User
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
    suspend fun getUserById(@Path("id") id: Int?): NetworkResponse<User>

    @POST("user/users/stuLogin")
    suspend fun loginCheck(@Body loginRequest: LoginRequest): NetworkResponse<User>

    @POST
    suspend fun checkPortrait(@Body pic: String) : NetworkResponse<Boolean>

    @GET("task")
    suspend fun getTasksByUserId(@Query("id") id: Int?): NetworkResponse<List<Task>>

    @GET("task")
    suspend fun getTaskDetail(@Query("userId") userId: Int?,@Query("taskId") taskId: Int?,): NetworkResponse<Task>


}

interface WfNetworkDataSource {
    suspend fun getUserById(id: Int? = null): User

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>

    suspend fun getTasksByUserId(id: Int? = null): List<Task>

    suspend fun checkPortrait(pic: String) :Boolean


}

@Serializable
data class NetworkResponse<T>(
    val msg: String, // 消息
    val code: Int, // 状态码
    val data: T,
//    val token: String, // 登录成功后返回的token
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

    override suspend fun getUserById(id: Int?): User = networkApi.getUserById(id).data
    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> = networkApi.loginCheck(loginRequest)
    override suspend fun getTasksByUserId(id: Int?): List<Task> = networkApi.getTasksByUserId(id).data
    override suspend fun checkPortrait(pic: String): Boolean = networkApi.checkPortrait(pic).data


}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsNetworkDataSource(impl: RetrofitWfNetwork): WfNetworkDataSource
}
