package com.example.welcome_freshman.core.notification

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 *@date 2024/3/14 10:32
 *@author GFCoder
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {
    @Binds
    abstract fun bindNotifier(
        notifier: SystemNotifier,
    ): Notifier
}