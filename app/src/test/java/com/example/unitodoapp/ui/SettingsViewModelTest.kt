package com.example.unitodoapp.ui

import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.datastore.UserPreferences
import com.example.unitodoapp.ui.screens.settings.SettingsState
import com.example.unitodoapp.ui.screens.settings.SettingsViewModel
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiEvent
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(PowerMockRunner::class)
@PrepareForTest(SettingsViewModel::class)
@PowerMockIgnore("javax.management.*")
class SettingsViewModelTest {

    private val dataStoreManager = mock<DataStoreManager>()
    private val _uiEvent = mock<Channel<SettingsUiEvent>>()
    private val uiEvent = mock<Flow<SettingsUiEvent>>()
    private val _uiState = mock<MutableStateFlow<SettingsState>>()
    private val uiState = mock<StateFlow<SettingsState>>()
    private val themeMode = mock<ThemeMode>()
    private val userPref = mock<UserPreferences>()
    private val userPreferences = mock<Flow<UserPreferences>>()

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        Mockito.`when`(themeMode).thenReturn(ThemeMode.LIGHT)
        Mockito.`when`(userPref.themeMode).thenReturn(themeMode)
        Mockito.`when`(userPref.isStayLogged).thenReturn(true)
        Mockito.`when`(userPref.email).thenReturn(EMAIL)
        Mockito.`when`(dataStoreManager.userPreferences).thenReturn(userPreferences)

        settingsViewModel = SettingsViewModel(dataStoreManager)

        ReflectionHelpers.setField(settingsViewModel, MUTABLE_STATE_FLOW_UI_EVENT, _uiEvent)
        ReflectionHelpers.setField(settingsViewModel, STATE_FLOW_UI_EVENT, uiEvent)
        ReflectionHelpers.setField(settingsViewModel, MUTABLE_STATE_FLOW_UI_STATE, _uiState)
        ReflectionHelpers.setField(settingsViewModel, STATE_FLOW_UI_STATE, uiState)
    }

    companion object {

        private const val MUTABLE_STATE_FLOW_UI_EVENT = "_uiEvent"
        private const val STATE_FLOW_UI_EVENT = "uiEvent"
        private const val MUTABLE_STATE_FLOW_UI_STATE = "_uiState"
        private const val STATE_FLOW_UI_STATE = "uiState"
        private const val EMAIL = "email"
    }
}
