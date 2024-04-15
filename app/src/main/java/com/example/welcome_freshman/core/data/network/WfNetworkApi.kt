package com.example.welcome_freshman.core.data.network

import com.example.welcome_freshman.core.data.model.AiData
import com.example.welcome_freshman.core.data.model.Comment
import com.example.welcome_freshman.core.data.model.HomeDto
import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.model.PageUtil
import com.example.welcome_freshman.core.data.model.PunchTask
import com.example.welcome_freshman.core.data.model.QuizDto
import com.example.welcome_freshman.core.data.model.SingleTaskRank
import com.example.welcome_freshman.core.data.model.SubTask
import com.example.welcome_freshman.core.data.model.Task
import com.example.welcome_freshman.core.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
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

    @POST("task/points/ValidFace")
    suspend fun checkPortraitTask(@Body validRequest: Map<String, String>): NetworkResponse<PunchTask>

    /**
     * 获取任务列表
     */
    @GET("task/points/getPoint/{studentId}")
    suspend fun getTasksByUserId(@Path("studentId") id: Int?): NetworkResponse<HomeDto>

    @GET("task/points/getDetail")
    suspend fun getTaskDetail(
        @Query("studentId") userId: Int?,
        @Query("taskId") taskId: Int?,
    ): NetworkResponse<Task>

    @GET("ad/advertisements/images/{id}")
    suspend fun getAd(@Path("id") userId: Int?): NetworkResponse<String>

    @GET("ad/advertisements/noLoginGet")
    suspend fun getAdNoLogin(): NetworkResponse<String>

    @GET("task/points/getRankPoint")
    suspend fun getRank(): NetworkResponse<List<User>>

    @GET("user/communitys/infoByTaskId/{id}")
    suspend fun getCommentByTaskId(@Path("id") taskId: Int?): NetworkResponse<List<Comment>>

    /**
     *  身份证认证
     */
    @Multipart
    @POST("user/users/Homeidentity")
    suspend fun validIdCard(
        @Part identity: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ): NetworkResponse<String>

    @POST("user/communitys/save")
    suspend fun doComment(@Body comment: Comment): NetworkResponse<Int>

    @GET("task/taskquestion/getQ")
    suspend fun getQuizList(@Query("id") subTaskId: Int): NetworkResponse<List<QuizDto>>

    @GET("task/points/getRankAve")
    suspend fun getAveTimeRank(): NetworkResponse<List<User>>

    @POST("task/points/ValidQA")
    suspend fun quizCompleted(
        @Query("studentId") userId: Int?,
        @Query("taskId") taskId: Int?,
        @Query("result") res: Boolean
    ): NetworkResponse<Boolean>

    @POST("ai/openwxyy")
    suspend fun reqChat(@Body req: Map<String, String>): NetworkResponse<AiData>// https://luckycola.com.cn/ai/openwxyy

    @GET("task/points/getRankShort")
    suspend fun getRankShort(): NetworkResponse<List<SingleTaskRank>>

    @GET("task/region/list")
    suspend fun getRegionTask(): NetworkResponse<PageUtil>

    @Multipart
    @POST("task/points/ValidOCR")
    suspend fun checkLocation(
        @Part identity: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
        @Part("taskId") taskId: RequestBody,
    ): NetworkResponse<String>

    @Multipart
    @POST("user/users/admission")
    suspend fun checkOnNotice(
        @Part identityBody: MultipartBody.Part,
        @Part("userId") userIdRequestBody: RequestBody
    ): NetworkResponse<String>

}

interface WfNetworkDataSource {
    suspend fun getUserById(id: Int? = null): User?

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>

    suspend fun getTasksByUserId(id: Int? = null): HomeDto?

    suspend fun checkPortrait(validRequest: Map<String, String>): Int

    suspend fun updateUser(user: User): Int

    suspend fun uploadAvatar(avatarData: ByteArray, userId: Int): String?

    suspend fun getAd(userId: Int?): String?

    suspend fun getTaskDetail(userId: Int?, taskId: Int?): Task?
    suspend fun getRanks(id: Int): List<User>?

    suspend fun getCommentByTaskId(taskId: Int?): List<Comment>?

    suspend fun validIdCard(identity: ByteArray, userId: Int?): Int
    suspend fun checkPortraitTask(validRequest: Map<String, String>): Int
    suspend fun doComment(comment: Comment): Int
    suspend fun getQuizList(subTaskId: Int): List<QuizDto>?

    suspend fun quizCompleted(userId: Int?, taskId: Int?, res: Boolean): Boolean?

    suspend fun reqChat(chatReq: Map<String, String>): String?

    suspend fun getRankShort(): List<SingleTaskRank>?
    suspend fun getRegionTask(): List<SubTask>?

    suspend fun checkLocation(identity: ByteArray, userId: Int?, taskId: Int?): Int

    suspend fun getAdNoLogin(): String?
    suspend fun checkOnNotice(pic: ByteArray, userId: Int): Int

}

@Serializable
data class NetworkResponse<T>(
    val msg: String? = "", // 消息
    val code: Int, // 状态码
    val data: T? = null,
//    val token: String, // 登录成功后返回的token
)

private const val WF_BASE_URL = "http://192.168.215.138:88/api/"

@Singleton
class RetrofitWfNetwork @Inject constructor(
    private val networkJson: Json,
    factory: Call.Factory
) : WfNetworkDataSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl(WF_BASE_URL)
        .callFactory(factory)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()

    private val networkApi = retrofit.create(WfNetworkApi::class.java)

    override suspend fun getUserById(id: Int?): User? = networkApi.getUserById(id).data
    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> =
        networkApi.loginCheck(loginRequest)

    override suspend fun getTasksByUserId(id: Int?): HomeDto? =
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
    override suspend fun getTaskDetail(userId: Int?, taskId: Int?): Task? {
        val data = networkApi.getTaskDetail(userId, taskId).data
        return data
    }

    override suspend fun getRanks(id: Int): List<User>? = when (id) {
        0 -> networkApi.getRank().data
        1 -> networkApi.getAveTimeRank().data
        2 -> networkApi.getRank().data

        else -> networkApi.getRank().data
    }

    override suspend fun getCommentByTaskId(taskId: Int?): List<Comment>? =
        networkApi.getCommentByTaskId(taskId).data

    override suspend fun validIdCard(identity: ByteArray, userId: Int?): Int {
        val requestBody = identity.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val identityBody = MultipartBody.Part.createFormData("identity", "image.jpg", requestBody)
        val userIdRequestBody = userId.toString().toRequestBody("text/plain".toMediaType())

        return networkApi.validIdCard(identityBody, userIdRequestBody).code
    }

    override suspend fun checkPortraitTask(validRequest: Map<String, String>): Int =
        networkApi.checkPortraitTask(validRequest).code

    override suspend fun doComment(comment: Comment): Int =
        networkApi.doComment(comment).code

    override suspend fun getQuizList(subTaskId: Int): List<QuizDto>? =
        networkApi.getQuizList(subTaskId).data

    override suspend fun quizCompleted(userId: Int?, taskId: Int?, res: Boolean): Boolean? =
        networkApi.quizCompleted(userId, taskId, res).data

    override suspend fun reqChat(chatReq: Map<String, String>): String? {
        val modifiedRetrofit = retrofit.newBuilder().baseUrl("https://luckycola.com.cn/")
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
        val newNetworkApi = modifiedRetrofit.create(WfNetworkApi::class.java)
        val res = newNetworkApi.reqChat(chatReq).data?.result
        return res
    }

    override suspend fun getRankShort(): List<SingleTaskRank>? = networkApi.getRankShort().data
    override suspend fun getRegionTask(): List<SubTask>? = networkApi.getRegionTask().data?.list
    override suspend fun checkLocation(identity: ByteArray, userId: Int?, taskId: Int?): Int {
        val requestBody = identity.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val identityBody = MultipartBody.Part.createFormData("locate", "image.jpg", requestBody)
        val userIdRequestBody = userId.toString().toRequestBody("text/plain".toMediaType())
        val taskIdRequestBody = taskId.toString().toRequestBody("text/plain".toMediaType())

        return networkApi.checkLocation(identityBody, userIdRequestBody, taskIdRequestBody).code
    }

    override suspend fun getAdNoLogin(): String? = networkApi.getAdNoLogin().data
    override suspend fun checkOnNotice(pic: ByteArray, userId: Int): Int {
        val requestBody = pic.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val identityBody = MultipartBody.Part.createFormData("admission", "image.jpg", requestBody)
        val userIdRequestBody = userId.toString().toRequestBody("text/plain".toMediaType())

        return networkApi.checkOnNotice(identityBody, userIdRequestBody).code
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsNetworkDataSource(impl: RetrofitWfNetwork): WfNetworkDataSource
}
