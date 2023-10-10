package com.example.unitodoapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private val Context.protoDataStore by dataStore("settings.json", SettingsSerializer)

class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.protoDataStore

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        settingsDataStore.updateData { data ->
            data.copy(themeMode = themeMode)
        }
    }

    fun getSettings() = settingsDataStore.data
}
