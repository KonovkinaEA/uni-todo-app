package com.example.unitodoapp.ui.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsUiEventHandler(
    uiEvent: Flow<SettingsUiEvent>,
    onNavigateUp: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect {
            when (it) {
                SettingsUiEvent.NavigateUp -> onNavigateUp()
            }
        }
    }
}
