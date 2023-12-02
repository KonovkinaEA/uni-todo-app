package com.example.unitodoapp.ui

import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.datastore.UserPreferences
import com.example.unitodoapp.ui.screens.settings.SettingsViewModel
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.fest.assertions.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val userPref = mockk<UserPreferences>(relaxed = true) {
        every { themeMode } returns ThemeMode.DARK
        every { isStayLogged } returns true
        every { email } returns EMAIL
    }
    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true) {
        every { userPreferences } returns MutableStateFlow(userPref)
    }

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() = runTest {
        settingsViewModel = SettingsViewModel(dataStoreManager)
        advanceUntilIdle()
    }

    @Test
    fun testInitialization() {
        val value = settingsViewModel.uiState.value

        Assertions.assertThat(value.themeMode).isEqualTo(ThemeMode.DARK)
        Assertions.assertThat(value.isUserStayLogged).isEqualTo(true)
        Assertions.assertThat(value.email).isEqualTo(EMAIL)
    }

    companion object {

        private const val EMAIL = "email"
    }
}
