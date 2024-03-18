package com.example.welcome_freshman.core.data.network

import android.net.Uri
import androidx.core.net.toUri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/6 19:03
 *@author GFCoder
 */

private interface WfNetworkApi {
    @GET("user/users/info/{id}")
    suspend fun getUserById(@Path("id") id: Int?): NetworkResponse<User>

    @POST("user/users/update")
    suspend fun updateUser(@Body user: User): NetworkResponse<Boolean>

    @Multipart
    @POST("user/users/upload")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
    ): NetworkResponse<String>

    @POST("user/users/stuLogin")
    suspend fun loginCheck(@Body loginRequest: LoginRequest): NetworkResponse<User>

    @POST("user/users/validFace")
    suspend fun checkPortrait(@Body validRequest: Map<String, String>): NetworkResponse<Boolean>

    @GET("task/tasks/list/{id}")
    suspend fun getTasksByUserId(@Path("id") id: Int?): NetworkResponse<List<Task>>

    @GET("task")
    suspend fun getTaskDetail(
        @Query("userId") userId: Int?,
        @Query("taskId") taskId: Int?,
    ): NetworkResponse<Task>

    @GET("ad/advertisements/images/{id}")
    suspend fun getAd(@Path("id") userId: Int?): NetworkResponse<String>

}

interface WfNetworkDataSource {
    suspend fun getUserById(id: Int? = null): User?

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>

    suspend fun getTasksByUserId(id: Int? = null): List<Task>?

    suspend fun checkPortrait(validRequest: Map<String, String>): Int

    suspend fun updateUser(user: User): Int

    suspend fun uploadAvatar(avatarData: ByteArray, userId: Int): String?

    suspend fun getAd(@Path("id") userId: Int?): String?


}

@Serializable
data class NetworkResponse<T>(
    val msg: String?, // 消息
    val code: Int, // 状态码
    val data: T? = null,
//    val token: String, // 登录成功后返回的token
)

private const val WF_BASE_URL = "http://192.168.246.79:88/api/"

@Singleton
class RetrofitWfNetwork @Inject constructor(
    networkJson: Json
) : WfNetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(WF_BASE_URL)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(WfNetworkApi::class.java)

    override suspend fun getUserById(id: Int?): User? = networkApi.getUserById(id).data
    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> =
        networkApi.loginCheck(loginRequest)

    override suspend fun getTasksByUserId(id: Int?): List<Task>? =
        networkApi.getTasksByUserId(id).data

    override suspend fun checkPortrait(validRequest: Map<String, String>): Int =
        networkApi.checkPortrait(validRequest).code

    override suspend fun updateUser(user: User): Int = networkApi.updateUser(user).code

    override suspend fun uploadAvatar(
        avatarData: ByteArray,
        userId: Int
    ): String? {
        val requestBody = avatarData.toRequestBody("image/jpeg".toMediaTypeOrNull())
        // 创建MultipartBody.Part
        val avatarBody = MultipartBody.Part.createFormData("avatar", "image.jpg", requestBody)

        val userIdRequestBody = userId.toString().toRequestBody("text/plain".toMediaType())

        return networkApi.uploadAvatar(avatarBody, userIdRequestBody).msg
    }

    override suspend fun getAd(userId: Int?): String? = networkApi.getAd(userId).data


}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsNetworkDataSource(impl: RetrofitWfNetwork): WfNetworkDataSource
}
