package com.example.unitodoapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.data.navigation.LogIn
import com.example.unitodoapp.ui.components.settings.ProfileCard
import com.example.unitodoapp.ui.components.settings.SettingsTopAppBar
import com.example.unitodoapp.ui.components.settings.ThemePicker
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiEvent
import com.example.unitodoapp.ui.theme.ExtendedTheme

@Composable
fun SettingsScreen(navController: NavHostController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                SettingsUiEvent.NavigateUp -> {
                    navController.navigate(List.route) { popUpTo(List.route) { inclusive = true } }
                }

                SettingsUiEvent.NavigateToLogIn -> {
                    navController.navigate(LogIn.route) { popUpTo(LogIn.route) { inclusive = true } }
                }
            }
        }
    }

    Scaffold(
        topBar = { SettingsTopAppBar(uiAction = viewModel::onUiAction) },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {

            ProfileCard(
                uiState = uiState,
                onUiAction = viewModel :: onUiAction
            )
            ThemePicker(
                themeMode = uiState.themeMode,
                uiAction = viewModel::onUiAction
            )
        }
    }
}
