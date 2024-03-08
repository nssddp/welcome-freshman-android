package com.example.welcome_freshman.data.repository

import com.example.welcome_freshman.data.model.LoginRequest
import com.example.welcome_freshman.data.model.User
import com.example.welcome_freshman.data.network.NetworkResponse
import com.example.welcome_freshman.data.network.WfNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 *@date 2024/3/7 15:56
 *@author GFCoder
 */
interface UserRepository {
    suspend fun getUserById(id: String):User

    suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User>
}

class MainUserRepository @Inject constructor(
    private val network: WfNetworkDataSource
) : UserRepository {
    override suspend fun getUserById(id: String):User = network.getUserById(id)
    override suspend fun loginCheck(loginRequest: LoginRequest): NetworkResponse<User> = network.loginCheck(loginRequest)

}

@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsUserRepository(impl: MainUserRepository): UserRepository
}