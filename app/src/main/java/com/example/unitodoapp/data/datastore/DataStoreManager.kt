package com.example.unitodoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
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

}
