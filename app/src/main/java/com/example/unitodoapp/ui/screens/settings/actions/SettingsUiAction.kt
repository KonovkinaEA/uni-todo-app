package com.example.unitodoapp.ui.screens.settings.actions

import com.example.unitodoapp.ui.screens.settings.model.ThemeMode

sealed class SettingsUiAction {
    data class UpdateThemeMode(val themeMode: ThemeMode) : SettingsUiAction()
    object LogOutUser: SettingsUiAction()
    object NavigateUp: SettingsUiAction()
    object NavigateToLoginScreen: SettingsUiAction()
}
