package com.example.unitodoapp.ui.screens.authorization.actions

sealed class AuthUiEvent {
    object NavigateToLog : AuthUiEvent()
    object NavigateToList : AuthUiEvent()
}
