package com.example.welcome_freshman.core.data.datastore

import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/10 12:26
 *@author GFCoder
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private val userDataStore = context.dataStore

    private val userId = intPreferencesKey("user_id")

    val userIdFlow : Flow<Int> = userDataStore.data.map {  userData ->
        userData[userId] ?:0
    }
    suspend fun setUserId(id: Int) {
        userDataStore.edit {userData ->
            userData[userId] = id
        }
    }
}