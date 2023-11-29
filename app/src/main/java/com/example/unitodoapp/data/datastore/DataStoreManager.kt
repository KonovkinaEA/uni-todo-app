package com.example.unitodoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.unitodoapp.data.model.User
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.protoDataStore by dataStore("settings.json", PreferencesSerializer)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val preferencesDataStore = appContext.protoDataStore
    val userPreferences = preferencesDataStore.data

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(themeMode = themeMode)
        }
    }

    suspend fun saveNotificationsPermission(notifyPermissionGranted: Boolean) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(notifyPermissionGranted = notifyPermissionGranted)
        }
    }

    suspend fun saveFilterState(isFiltered: Boolean) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(isListFilter = isFiltered)
        }
    }

    suspend fun saveUser(user: User) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(
                email = user.email,
                password = user.password
            )
        }
    }

    suspend fun logOutUser() {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(
                email = null,
                password = null
            )
        }
    }

    suspend fun setUserStayLoggedTo(value: Boolean) {
        preferencesDataStore.updateData { userPreferences ->
            userPreferences.copy(
                isStayLogged = value
            )
        }
    }
}
