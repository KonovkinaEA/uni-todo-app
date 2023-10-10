package com.example.unitodoapp.ui.screens.edit.actions

sealed class EditUiEvent {
    object NavigateUp: EditUiEvent()
    object SaveTask: EditUiEvent()
}
