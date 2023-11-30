package com.example.unitodoapp.ui.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel.TextFieldType.CONF
import com.example.unitodoapp.ui.screens.authorization.AuthViewModel.TextFieldType.EML
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
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()


    fun onUiAction(action: AuthUiAction) {
        when (action) {
            is AuthUiAction.RegisterNewUser ->
                viewModelScope.launch {
                    registerNewUser(action.user)
                }

            is AuthUiAction.UpdateLogin -> updateTextField(action.email, EML)
            is AuthUiAction.UpdatePass -> updateTextField(action.pass, PASS)
            is AuthUiAction.UpdateConfirmPass -> updateTextField(action.confPass, CONF)
            AuthUiAction.UpdatePassVisibility -> updateVisibility(PASS)
            AuthUiAction.UpdateConfPassVisibility -> updateVisibility(CONF)
            is AuthUiAction.LogInUser ->
                viewModelScope.launch {
                    logInUser(action.user)
                }

            is AuthUiAction.UpdatePassForUser ->
                viewModelScope.launch {
                    passUpdateForUser(action.user)
                }

            is AuthUiAction.UpdateRemUserCheckbox -> {
                _uiState.update {
                    uiState.value.copy(
                        isUserRemembered = action.isCheck
                    )
                }
            }
        }
    }

    private suspend fun passUpdateForUser(user: User) {
        val isUserExist = repository.checkUserExist(user)
        var isValidationPassed = true

        if (!isUserExist) { // login doesn't exist in system
            _uiState.update {
                uiState.value.copy(
                    isEmailValid = false,
                    emailErrorMassage = "There is no user with such email"
                )
            }
            isValidationPassed = false
        }

        if (!isPassValid(user.password)) {
            isValidationPassed = false
            _uiState.update {
                uiState.value.copy(
                    isPassValid = false,
                    passErrorMassage = "Invalid password. Use $MIN_LOGIN_LEN-$MAX_LOGIN_LEN characters with only letters (a-z), digits, and '_', '-'"
                )
            }
        }

        if (uiState.value.password != uiState.value.confPassword)
            isValidationPassed = false


        if (isValidationPassed) {
            repository.recoveryPassword(user)
            viewModelScope.launch {
                _uiEvent.send(AuthUiEvent.NavigateToLog)
            }
        }
    }

    private suspend fun logInUser(user: User) {
        val isSuccess = repository.logInUser(user = user)

        if (!isSuccess) {
            _uiState.update {
                uiState.value.copy(
                    isPassValid = false,
                    isEmailValid = false,
                    passErrorMassage = "Incorrect email or password"
                )
            }
        } else {
            viewModelScope.launch {
                if (uiState.value.isUserRemembered) {
                    dataStoreManager.setUserStayLoggedTo(true)
                }
                dataStoreManager.saveUser(user)
                _uiEvent.send(AuthUiEvent.NavigateToList)
            }
        }
    }

    private fun updateVisibility(type: TextFieldType) {
        _uiState.update {
            when (type) {
                EML -> {
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
                EML -> uiState.value.copy(
                    email = value,
                    isEmailValid = true,
                    emailErrorMassage = "",
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

    private suspend fun registerNewUser(user: User) {
        var isValidationPassed = true

        if (!isEmailValid(user.email)) {
            isValidationPassed = false
            _uiState.update {
                uiState.value.copy(
                    isEmailValid = false,
                    emailErrorMassage = "Invalid email"
                )
            }
        }

        if (!isPassValid(user.password)) {
            isValidationPassed = false
            _uiState.update {
                uiState.value.copy(
                    isPassValid = false,
                    passErrorMassage = "Invalid password. Use $MIN_LOGIN_LEN-$MAX_LOGIN_LEN characters with only letters (a-z), digits, and '_', '-'"
                )
            }
        }

        if (uiState.value.password != uiState.value.confPassword)
            isValidationPassed = false

        if (isValidationPassed) {

            val isSuccess = repository.registerNewUser(user = user)

            if (isSuccess) { // login doesn't occupied
                viewModelScope.launch {
                    _uiEvent.send(AuthUiEvent.NavigateToLog)
                }
            } else {
                _uiState.update {
                    uiState.value.copy(
                        isEmailValid = false,
                        emailErrorMassage = "This email is already occupied."
                    )
                }
            }
        }
    }

    private fun isPassValid(password: String): Boolean =
        password.matches("^[a-zA-Z\\d_{-]{${MIN_PASS_LEN},$MAX_PASS_LEN}\$".toRegex())


    private fun isEmailValid(email: String): Boolean {
        val pattern = "(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#${'$'}" +
                "%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" +
                "\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9]" +
                ")?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}" +
                "(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]" +
                ":(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

        return email.matches(pattern.toRegex())
    }


    enum class TextFieldType { EML, PASS, CONF }

}

data class AuthUiState(
    val screen: Screen = Screen.WELCOME,

    val email: String = "",
    val isEmailValid: Boolean = true,
    val emailErrorMassage: String = "",

    val password: String = "",
    val isPassValid: Boolean = true,
    val isPassVisible: Boolean = false,
    val passErrorMassage: String = "",

    val confPassword: String = "",
    val isPassConfVisible: Boolean = false,
    val confPassErrorMassage: String = "",

    val isUserRemembered: Boolean = false
)

enum class Screen { WELCOME, REG, LOGIN, PASSREC }

