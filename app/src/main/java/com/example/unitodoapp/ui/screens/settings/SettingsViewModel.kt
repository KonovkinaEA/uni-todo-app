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
            dataStoreManager.userPreferences.collectLatest {
                _uiState.value = SettingsState(it.themeMode)
            }
        }
    }

    fun onUiAction(action: SettingsUiAction) {
        when (action) {
            SettingsUiAction.NavigateUp -> viewModelScope.launch {
                _uiEvent.send(SettingsUiEvent.NavigateUp)
            }
            is SettingsUiAction.UpdateThemeMode -> {
                _uiState.value = SettingsState(action.themeMode)

                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreManager.saveThemeMode(action.themeMode)
                }
            }
        }
    }
}

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.LIGHT
)
