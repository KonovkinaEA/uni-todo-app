package com.example.unitodoapp.ui.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.model.User
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel.TextFieldType.CONF
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel.TextFieldType.LOG
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel.TextFieldType.PASS
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiAction
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiEvent
import com.example.unitodoapp.utils.MAX_LOGIN_LEN
import com.example.unitodoapp.utils.MAX_PASS_LEN
import com.example.unitodoapp.utils.MIN_LOGIN_LEN
import com.example.unitodoapp.utils.MIN_PASS_LEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

) : ViewModel() {

    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()


    fun onUiAction(action: AuthUiAction) {
        when (action) {
            is AuthUiAction.RegisterNewUser -> registerNewUser(action.user)
            is AuthUiAction.UpdateLogin -> updateTextField(action.login, LOG)
            is AuthUiAction.UpdatePass -> updateTextField(action.pass, PASS)
            is AuthUiAction.UpdateConfirmPass -> updateTextField(action.confPass, CONF)
            AuthUiAction.UpdatePassVisibility -> updateVisibility(PASS)
            AuthUiAction.UpdateConfPassVisibility -> updateVisibility(CONF)
            is AuthUiAction.LogInUser -> logInUser(action.user)
            is AuthUiAction.UpdatePassForUser -> passUpdateForUser(action.user)
        }
    }

    private fun passUpdateForUser(user: User) {
        var isPassUpdateSuccess = true

        //network check and update pass

        if (isPassUpdateSuccess) {
            viewModelScope.launch {
                _uiEvent.send(AuthUiEvent.NavigateToLog)
            }
        }
    }

    private fun logInUser(user: User) {
        var isUserValid = true
        // network check that user valid
        if (isUserValid) {
            viewModelScope.launch {
                _uiEvent.send(AuthUiEvent.NavigateToList)
            }
        }
    }

    private fun updateVisibility(type: TextFieldType) {
        _uiState.update {
            when (type) {
                LOG -> {
                    uiState.value
                }

                PASS -> {
                    uiState.value.copy(isPassVisible = !uiState.value.isPassVisible)
                }

                CONF -> {
                    uiState.value.copy(isPassConfVisible = !uiState.value.isPassConfVisible)
                }
            }
        }
    }

    private fun updateTextField(value: String, type: TextFieldType) {
        _uiState.update {
            when (type) {
                LOG -> uiState.value.copy(
                    login = value,
                    isLoginValid = true,
                    loginErrorMassage = "",
                )

                PASS -> uiState.value.copy(
                    password = value,
                    isPassValid = true,
                    passErrorMassage = "",
                )
                CONF -> uiState.value.copy(confPassword = value)
            }
        }
    }

    private fun registerNewUser(user: User) {
        var isRegistrationSuccess: Boolean = true

        if (!loginValidation(user.login)) {
            isRegistrationSuccess = false
            _uiState.update {
                uiState.value.copy(
                    isLoginValid = false,
                    loginErrorMassage = "Invalid login. Use $MIN_LOGIN_LEN-$MAX_LOGIN_LEN characters with only letters (a-z), digits, and '_'."
                )
            }
        }

        if (!passValidation(user.password)) {
            isRegistrationSuccess = false
            _uiState.update {
                uiState.value.copy(
                    isPassValid = false,
                    passErrorMassage = "Invalid password. Use $MIN_LOGIN_LEN-$MAX_LOGIN_LEN characters with only letters (a-z), digits, and '_', '-'"
                )
            }
        }

        //some network work
        if (isRegistrationSuccess) {
            viewModelScope.launch {
                _uiEvent.send(AuthUiEvent.NavigateToLog)
            }
        }
    }

    private fun passValidation(password: String): Boolean =
        password.matches("^[a-zA-Z\\d_-]{${MIN_PASS_LEN},$MAX_PASS_LEN}\$".toRegex())


    private fun loginValidation(login: String): Boolean =
        login.matches("^[a-zA-Z\\d_]{$MIN_LOGIN_LEN,$MAX_LOGIN_LEN}\$".toRegex())

    enum class TextFieldType { LOG, PASS, CONF }

}

data class AuthUiState(
    val screen: Screen = Screen.WELCOME,

    val login: String = "",
    val isLoginValid: Boolean = true,
    val loginErrorMassage: String = "",

    val password: String = "",
    val isPassValid: Boolean = true,
    val isPassVisible: Boolean = false,
    val passErrorMassage: String = "",

    val confPassword: String = "",
    val isPassConfVisible: Boolean = false,
    val confPassErrorMassage: String = ""
)

enum class Screen { WELCOME, REG, LOGIN, PASSREC }

