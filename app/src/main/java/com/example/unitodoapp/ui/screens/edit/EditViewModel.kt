package com.example.unitodoapp.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.data.navigation.Edit
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.screens.edit.actions.EditUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var todoItem = TodoItem()
    private var isNewItem: Boolean = true

    private val _uiEvent = Channel<EditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>(Edit.id) ?: ""
            repository.getItem(id)?.let { item ->
                todoItem = item
                isNewItem = false

                _uiState.update { uiState.value.copy(
                    text = item.text,
                    importance = item.importance,
                    deadline = item.deadline ?: uiState.value.deadline,
                    isDeadlineSet = item.deadline != null,
                    isNewItem = false
                ) }
            }
        }
    }

    fun onUiAction(action: EditUiAction) {
        when(action) {
            EditUiAction.SaveTask -> saveTodoItem()
            EditUiAction.DeleteTask -> removeTodoItem()
            EditUiAction.NavigateUp -> viewModelScope.launch {
                _uiEvent.send(EditUiEvent.NavigateUp)
            }
            is EditUiAction.UpdateText -> _uiState.update {
                uiState.value.copy(text = action.text)
            }
            is EditUiAction.UpdateDeadlineSet -> _uiState.update {
                uiState.value.copy(isDeadlineSet = action.isDeadlineSet)
            }
            is EditUiAction.UpdateImportance -> _uiState.update {
                uiState.value.copy(importance = action.importance)
            }
            is EditUiAction.UpdateDeadline -> _uiState.update {
                uiState.value.copy(deadline = action.deadline)
            }
        }
    }

    private fun saveTodoItem() {
        if (uiState.value.text.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            if (isNewItem) repository.addItem(todoItem) else repository.updateItem(todoItem)
            _uiEvent.send(EditUiEvent.SaveTask)
        }
    }

    private fun removeTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isNewItem) repository.removeItem(todoItem.id)
            _uiEvent.send(EditUiEvent.NavigateUp)
        }
    }
}

data class EditUiState(
    val text: String = "",
    val importance: Importance = Importance.LOW,
    val deadline: Long = System.currentTimeMillis(),
    val isDeadlineSet: Boolean = false,
    val isNewItem: Boolean = true
) {
    val isDeleteEnabled: Boolean
        get() = text.isNotBlank() || !isNewItem
}
