package com.example.welcome_freshman.core.data.repository.di

import com.example.welcome_freshman.core.data.repository.AdRepository
import com.example.welcome_freshman.core.data.repository.MainAdRepository
import com.example.welcome_freshman.core.data.repository.MainTaskRepository
import com.example.welcome_freshman.core.data.repository.MainUserRepository
import com.example.welcome_freshman.core.data.repository.TaskRepository
import com.example.welcome_freshman.core.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 *@date 2024/3/11 11:02
 *@author GFCoder
 */
@Module
@InstallIn(SingletonComponent::class)
interface WfNetworkModule {
    @Binds
    fun bindsUserRepository(impl: MainUserRepository): UserRepository

    @Binds
    fun bindsTaskRepository(impl: MainTaskRepository): TaskRepository

    @Binds
    fun bindsAdRepository(impl: MainAdRepository): AdRepository

}