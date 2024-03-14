package com.example.welcome_freshman.core.data.repository

import com.example.welcome_freshman.UserPreferences
import com.example.welcome_freshman.core.data.datastore.WfPreferencesDataSource
import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.network.NetworkResponse
import com.example.welcome_freshman.core.data.network.WfNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import javax.inject.Inject

/**
 *@date 2024/3/7 15:56
 *@author GFCoder
 */
interface UserRepository {
    val userData: Flow<UserPreferences>

    suspend fun setUserId(id: Int)

    suspend fun getUserById(id: Int): User

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>

    suspend fun checkPortrait(pic: String): Boolean

}

class MainUserRepository @Inject constructor(
    private val network: WfNetworkDataSource,
//    private val dataStoreManager: DataStoreManager,
    private val preferencesDataSource: WfPreferencesDataSource
) : UserRepository {

    override val userData: Flow<UserPreferences> = preferencesDataSource.userData

    override suspend fun setUserId(id: Int) = preferencesDataSource.setUserId(id)

    override suspend fun getUserById(id: Int): User = network.getUserById(id)

    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> =
        network.loginCheck(loginRequest)

    override suspend fun checkPortrait(pic: String): Boolean {
        /*val jsonObject = JSONObject()
        jsonObject.put("imageBase64", pic)*/
        return network.checkPortrait(pic)
    }


}

