package com.example.unitodoapp.ui.screens.authorization.actions

import com.example.unitodoapp.data.api.model.User

sealed class AuthUiAction {
    data class RegisterNewUser(val user: User) : AuthUiAction()
    data class UpdateLogin(val email: String) : AuthUiAction()
    data class UpdatePass(val pass: String) : AuthUiAction()
    data class UpdateConfirmPass(val confPass: String) : AuthUiAction()
    object UpdatePassVisibility : AuthUiAction()
    object UpdateConfPassVisibility : AuthUiAction()
    data class LogInUser(val user: User) : AuthUiAction()
    data class UpdatePassForUser(val user: User) : AuthUiAction()
    data class UpdateRemUserCheckbox(val isCheck: Boolean) : AuthUiAction()

}
