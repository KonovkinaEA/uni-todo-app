package com.example.unitodoapp.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.ui.components.edit.Deadline
import com.example.unitodoapp.ui.components.edit.DeleteButton
import com.example.unitodoapp.ui.components.edit.EditDivider
import com.example.unitodoapp.ui.components.edit.Importance
import com.example.unitodoapp.ui.components.edit.TextField
import com.example.unitodoapp.ui.components.edit.EditTopAppBar
import com.example.unitodoapp.ui.screens.edit.actions.EditUiEvent
import com.example.unitodoapp.ui.theme.ExtendedTheme

@Composable
fun EditScreen(
    navController: NavHostController
) {
    val viewModel: EditViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                EditUiEvent.NavigateUp -> {
                    navController.navigate(List.route) { popUpTo(List.route) { inclusive = true } }
                }

                EditUiEvent.SaveTask -> {
                    navController.navigate(List.route) { popUpTo(List.route) { inclusive = true } }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            EditTopAppBar(
                text = uiState.text,
                uiAction = viewModel::onUiAction
            )
        },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            TextField(
                text = uiState.text,
                uiAction = viewModel::onUiAction
            )
            Importance(
                importance = uiState.importance,
                uiAction = viewModel::onUiAction
            )
            EditDivider(padding = PaddingValues(horizontal = 16.dp))
            Deadline(
                deadline = uiState.deadline,
                isDateVisible = uiState.isDeadlineSet,
                uiAction = viewModel::onUiAction
            )
            EditDivider(padding = PaddingValues(top = 16.dp, bottom = 8.dp))
            DeleteButton(
                enabled = uiState.isDeleteEnabled,
                uiAction = viewModel::onUiAction
            )
        }
    }
}
