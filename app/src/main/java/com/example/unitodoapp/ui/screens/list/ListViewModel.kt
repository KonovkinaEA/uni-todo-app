package com.example.unitodoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.screens.edit.actions.ListUiAction
import com.example.unitodoapp.ui.screens.edit.actions.ListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor() : ViewModel() {

    private val _uiEvent = Channel<ListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

    suspend fun getTodoItems() /*: Flow<List<TodoItem>>*/ {
        /*TODO*/
    }

    //fun errorListLiveData(): LiveData<Boolean> = repository.errorListLiveData()

    //fun errorItemLiveData(): LiveData<Boolean> = repository.errorItemLiveData()

    fun reloadData() {
        /*TODO*/
    }

    private fun updateTodoItem(todoItem: TodoItem) {
        /*TODO*/
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        /*TODO*/
    }
}