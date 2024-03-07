package com.example.welcome_freshman.data.network

import com.example.welcome_freshman.data.model.Task
import com.example.welcome_freshman.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/6 19:03
 *@author GFCoder
 */

private interface WfNetworkApi {
    @GET(value = "user/users")
    suspend fun getUserById(@Path("id") id: String?): NetworkResponse<Flow<User>>

    @GET(value = "task")
    suspend fun getTaskById(@Query("id") ids: String?): NetworkResponse<Task>

    /*@GET(value = "changelists/topics")
    suspend fun getTopicChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>*/
}

interface WfNetworkDataSource {
    suspend fun getUserById(id: String? = null): Flow<User>

    suspend fun getTaskById(id: String? = null): Task
}

@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

private const val WF_BASE_URL = "http://localhost:11000/"

@Singleton
class RetrofitWfNetwork @Inject constructor(
    networkJson: Json
) : WfNetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(WF_BASE_URL)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(WfNetworkApi::class.java)

    override suspend fun getUserById(id: String?): Flow<User> = networkApi.getUserById(id).data

    override suspend fun getTaskById(id: String?): Task = networkApi.getTaskById(id).data
}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsNetworkDataSource(impl: RetrofitWfNetwork): WfNetworkDataSource
}
