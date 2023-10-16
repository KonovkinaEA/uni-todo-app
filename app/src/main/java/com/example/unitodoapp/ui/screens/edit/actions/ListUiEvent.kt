package com.example.unitodoapp.ui.screens.edit.actions

sealed class ListUiEvent {
    data class NavigateToEditTodoItem(val id: String): ListUiEvent()
    object NavigateToNewTodoItem: ListUiEvent()
    object NavigateToSettings: ListUiEvent()
}