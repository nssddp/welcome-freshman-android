package com.example.welcome_freshman.core.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.welcome_freshman.UserPreferences
import com.example.welcome_freshman.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
    val userData: Flow<UserPreferences> = userPreferences.data //All your pref will be emitted here
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("UserPreferencesRepo", "Error reading sort order preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
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
}