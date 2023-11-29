package com.example.unitodoapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiAction
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiEvent
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiEvent = Channel<SettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.userPreferences.collectLatest { userPref ->

                val isLogged = userPref.email != null

                _uiState.value = SettingsState(
                    themeMode = userPref.themeMode,
                    isUserLogged = isLogged,
                    email = userPref.email
                )
            }
        }
    }

    fun onUiAction(action: SettingsUiAction) {
        when (action) {
            SettingsUiAction.NavigateUp -> viewModelScope.launch {
                _uiEvent.send(SettingsUiEvent.NavigateUp)
            }
            is SettingsUiAction.UpdateThemeMode -> {
                _uiState.update {
                    uiState.value.copy(
                        themeMode = action.themeMode
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreManager.saveThemeMode(action.themeMode)
                }
            }

            SettingsUiAction.LogOutUser -> {
                _uiState.update {
                    uiState.value.copy(
                        isUserLogged = false,
                        email = null
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreManager.logOutUser()
                }
            }

            SettingsUiAction.NavigateToLoginScreen -> viewModelScope.launch {
                _uiEvent.send(SettingsUiEvent.NavigateToLogIn)
            }
        }
    }
}

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val isUserLogged: Boolean = false,
    val email: String? = null
)
