package com.example.welcome_freshman.core.data.repository

import android.net.Uri
import com.example.welcome_freshman.UserPreferences
import com.example.welcome_freshman.core.data.datastore.WfPreferencesDataSource
import com.example.welcome_freshman.core.data.model.LoginRequest
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.data.network.NetworkResponse
import com.example.welcome_freshman.core.data.network.WfNetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *@date 2024/3/7 15:56
 *@author GFCoder
 */
interface UserRepository {
    val userData: Flow<UserPreferences>

    suspend fun setUserId(id: Int)
    suspend fun setValidState(state: Boolean)

    suspend fun getUserById(id: Int): User?

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>

    suspend fun checkPortrait(pic: String, stuId: Int): Int

    suspend fun updateUser(user: User): Int

    suspend fun uploadAvatar(avatarData: ByteArray, userId: Int): String?

}

class MainUserRepository @Inject constructor(
    private val network: WfNetworkDataSource,
//    private val dataStoreManager: DataStoreManager,
    private val preferencesDataSource: WfPreferencesDataSource
) : UserRepository {

    override val userData: Flow<UserPreferences> = preferencesDataSource.userData

    override suspend fun setUserId(id: Int) = preferencesDataSource.setUserId(id)

    override suspend fun setValidState(state: Boolean) = preferencesDataSource.setValidState(state)

    override suspend fun getUserById(id: Int): User? = network.getUserById(id)

    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> =
        network.loginCheck(loginRequest)

    override suspend fun checkPortrait(pic: String, stuId: Int): Int =
        network.checkPortrait(mapOf(Pair("imageBase64", pic), Pair("stuId", stuId.toString())))

    override suspend fun updateUser(user: User): Int = network.updateUser(user)

    override suspend fun uploadAvatar(avatarData: ByteArray, userId: Int): String? =
        network.uploadAvatar(avatarData, userId)

}

