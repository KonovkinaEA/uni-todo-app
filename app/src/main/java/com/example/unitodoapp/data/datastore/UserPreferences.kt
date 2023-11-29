package com.example.unitodoapp.data.datastore

import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val notifyPermissionGranted: Boolean = false,
    val isListFilter: Boolean = false,
    val email: String? = null,
    val password: String? = null
)