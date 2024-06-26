package com.example.unitodoapp.ui.screens.list.actions

import com.example.unitodoapp.data.model.TodoItem

sealed class ListUiAction {

    object AddNewItem : ListUiAction()
    data class ChangeFilter(val isFiltered: Boolean) :ListUiAction()
    data class EditTodoItem(val todoItem: TodoItem) : ListUiAction()
    data class UpdateTodoItem(val todoItem: TodoItem): ListUiAction()
    data class RemoveTodoItem(val todoItem: TodoItem) : ListUiAction()
}
