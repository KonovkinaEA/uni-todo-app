package com.example.unitodoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.screens.list.actions.ListUiAction
import com.example.unitodoapp.ui.screens.list.actions.ListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiEvent = Channel<ListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val todoItems: StateFlow<List<TodoItem>> = repository.todoItems

    fun onUiAction(action: ListUiAction) {
        when (action) {
            ListUiAction.AddNewItem -> {
                viewModelScope.launch {
                    _uiEvent.send(ListUiEvent.NavigateToNewTodoItem)
                }
            }

            is ListUiAction.EditTodoItem -> {
                viewModelScope.launch {
                    _uiEvent.send(ListUiEvent.NavigateToEditTodoItem(action.todoItem.id))
                }
            }

            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItem)
            is ListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItem)
        }
    }

    fun reloadData() {
        /*TODO*/
    }

    private fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(todoItem)
        }
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeItem(todoItem.id)
        }
    }
}
