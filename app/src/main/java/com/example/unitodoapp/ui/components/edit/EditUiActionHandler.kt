package com.example.unitodoapp.ui.components.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.unitodoapp.ui.screens.edit.actions.EditUiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun EditUiActionHandler(
    uiEvent: Flow<EditUiEvent>,
    onNavigateUp: () -> Unit,
    onSave: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect {
            when(it) {
                EditUiEvent.NavigateUp -> onNavigateUp()
                EditUiEvent.SaveTodoItem -> onSave()
            }
        }
    }
}
