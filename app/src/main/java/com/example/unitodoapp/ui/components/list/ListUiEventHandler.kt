package com.example.unitodoapp.ui.components.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.unitodoapp.ui.screens.edit.actions.ListUiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun ListUiEventHandler(
    uiEvent: Flow<ListUiEvent>,
    onNavigateToEditScreen: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect {
            when (it) {
                ListUiEvent.NavigateToNewTodoItem -> onNavigateToEditScreen()
                ListUiEvent.NavigateToSettings -> onNavigateToSettingsScreen()
                is ListUiEvent.NavigateToEditTodoItem -> TODO()
            }
        }
    }
}