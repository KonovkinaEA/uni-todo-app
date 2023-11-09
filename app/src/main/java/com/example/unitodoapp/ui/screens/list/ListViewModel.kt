package com.example.unitodoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.screens.list.actions.ListUiAction
import com.example.unitodoapp.ui.screens.list.actions.ListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val userPreferencesFlow = dataStoreManager.userPreferences

    private val _uiEvent = Channel<ListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = combine(
        _uiState,
        repository.todoItems,
        userPreferencesFlow
    ) { state, tasks, userPreferences ->
        state.copy(
            todoItems = filterTasks(tasks, userPreferences.isListFilter),
            isFiltered = userPreferences.isListFilter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListUiState())

    private fun filterTasks(tasks: List<TodoItem>, isFiltered: Boolean): List<TodoItem> {

        val filteredTasks = if (isFiltered) {
            repository.undoneTodoItems()
        } else {
            tasks
        }

        return filteredTasks
    }

    fun onUiAction(action: ListUiAction) {
        when (action) {
            ListUiAction.AddNewItem -> {
                viewModelScope.launch {
                    _uiEvent.send(ListUiEvent.NavigateToNewTodoItem)
                }
            }

            is ListUiAction.ChangeFilter -> changeFilterState(action.isFiltered)

            is ListUiAction.EditTodoItem -> {
                viewModelScope.launch {
                    _uiEvent.send(ListUiEvent.NavigateToEditTodoItem(action.todoItem.id))
                }
            }

            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItem)
            is ListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItem)

        }
    }

    private fun changeFilterState(isFiltered: Boolean) {
//        _uiState.update {
//            _uiState.value.copy(
//                isFiltered = !it.isFiltered
//            )
//        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveFilterState(!isFiltered)
        }
    }


    private fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateItem(todoItem)
        }
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.removeItem(todoItem.id)
        }
    }
}

data class ListUiState(
    val todoItems: List<TodoItem> = emptyList(),
    val isFiltered: Boolean = false
)
