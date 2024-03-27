package com.example.welcome_freshman.core.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.welcome_freshman.DarkThemeConfigProto
import com.example.welcome_freshman.UserPreferences
import com.example.welcome_freshman.copy
import com.example.welcome_freshman.core.data.model.DarkThemeConfig
import com.example.welcome_freshman.core.data.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@date 2024/3/10 14:56
 *@author GFCoder
 */

@Singleton
class WfPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData: Flow<UserData> = userPreferences.data //All your pref will be emitted here
        .map {
            UserData(
                userId = it.userId,
                validState = it.validState,
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                    ->
                        DarkThemeConfig.FOLLOW_SYSTEM
                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
            )
        }
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("UserPreferencesRepo", "Error reading sort order preferences.", exception)
//                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun setUserId(id: Int) {//edit your preferences with different methods
        try {
            userPreferences.updateData {
                it.copy {
                    this.userId = id
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setValidState(state: Boolean) {//edit your preferences with different methods
        try {
            userPreferences.updateData {
                it.copy {
                    this.validState = state
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.darkThemeConfig = when (darkThemeConfig) {
                        DarkThemeConfig.FOLLOW_SYSTEM ->
                            DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM

                        DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                        DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }
}