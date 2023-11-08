package com.example.unitodoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.protoDataStore by dataStore("settings.json", SettingsSerializer)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.protoDataStore

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        settingsDataStore.updateData { settings ->
            settings.copy(themeMode = themeMode)
        }
    }

    suspend fun saveNotificationsPermission(notifyPermissionGranted: Boolean) {
        settingsDataStore.updateData { settings ->
            settings.copy(notifyPermissionGranted = notifyPermissionGranted)
        }
    }

    fun getSettings() = settingsDataStore.data
}
