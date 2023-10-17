package com.example.unitodoapp.ui.screens.list.actions

sealed class ListUiEvent {
    data class NavigateToEditTodoItem(val id: String): ListUiEvent()
    object NavigateToNewTodoItem: ListUiEvent()
    object NavigateToSettings: ListUiEvent()
}
