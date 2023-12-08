package com.example.unitodoapp.ui

import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.workmanager.CustomWorkManager
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiAction
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.fest.assertions.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val workManager =  mockk<CustomWorkManager>(relaxed = true)
    private val repository = mockk<Repository>(relaxed = true)
    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true)
    private val _uiEvent = mockk<Channel<AuthUiEvent>>(relaxed = true) {
        coEvery { send(AuthUiEvent.NavigateToLog) } just runs
        coEvery { send(AuthUiEvent.NavigateToList) } just runs
    }
    private val user = mockk<User>(relaxed = true) {
        every { email } returns "test@mail.ru"
        every { password } returns "Qwerty7"
    }
    private lateinit var authViewModel: AuthViewModel
    private lateinit var spyAuthViewModel: AuthViewModel

    @Before
    fun setUp() = runTest {
        authViewModel = AuthViewModel(workManager, repository, dataStoreManager)
        ReflectionHelpers.setField(authViewModel, "_uiEvent", _uiEvent)
        spyAuthViewModel = spyk(authViewModel, recordPrivateCalls = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testOnUiActionRegisterNewUser() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.RegisterNewUser(user))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["registerNewUser"](user) }
    }

    @Test
    fun testOnUiActionUpdateLogin() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdateLogin(user.email))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["updateTextField"]("test@mail.ru", AuthViewModel.TextFieldType.EML) }
    }

    @Test
    fun testOnUiActionUpdatePass() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdatePass(user.password))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["updateTextField"]("Qwerty7", AuthViewModel.TextFieldType.PASS) }
    }

    @Test
    fun testOnUiActionUpdateConfirmPass() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdateConfirmPass(user.password))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["updateTextField"]("Qwerty7", AuthViewModel.TextFieldType.CONF) }
    }

    @Test
    fun testOnUiActionUpdatePassVisibility() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdatePassVisibility)
        advanceUntilIdle()
        coVerify { spyAuthViewModel["updateVisibility"](AuthViewModel.TextFieldType.PASS) }
    }

    @Test
    fun testOnUiActionUpdateConfPassVisibility() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdateConfPassVisibility)
        advanceUntilIdle()
        coVerify { spyAuthViewModel["updateVisibility"](AuthViewModel.TextFieldType.CONF) }
    }

    @Test
    fun testOnUiActionLogInUser() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.LogInUser(user))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["logInUser"](user) }
    }

    @Test
    fun testOnUiActionUpdatePassForUser() = runTest {
        spyAuthViewModel.onUiAction(AuthUiAction.UpdatePassForUser(user))
        advanceUntilIdle()
        coVerify { spyAuthViewModel["passUpdateForUser"](user) }
    }

    @Test
    fun testOnUiActionUpdateRemUserCheckBox() = runTest {
        authViewModel.onUiAction(AuthUiAction.UpdateRemUserCheckbox(true))
        advanceUntilIdle()
        Assertions.assertThat(authViewModel.uiState.value.isUserRemembered).isEqualTo(true)
    }

}