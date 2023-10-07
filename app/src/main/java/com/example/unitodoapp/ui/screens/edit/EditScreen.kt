package com.example.unitodoapp.ui.screens.edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.components.edit.EditUiActionHandler
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.ui.components.edit.EditDeadline
import com.example.unitodoapp.ui.components.edit.EditDelete
import com.example.unitodoapp.ui.components.edit.EditDivider
import com.example.unitodoapp.ui.components.edit.EditImportance
import com.example.unitodoapp.ui.components.edit.EditTextField
import com.example.unitodoapp.ui.components.edit.EditTopAppBar
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview

@Composable
fun EditScreen(navController: NavHostController) {
    val viewModel: EditViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    EditUiActionHandler(
        uiEvent = viewModel.uiEvent,
        onNavigateUp = {
            navController.navigate(List.route) { popUpTo(List.route) { inclusive = true } }
        },
        onSave = {
            navController.navigate(List.route) { popUpTo(List.route) { inclusive = true } }
        }
    )

    Scaffold(
        topBar = {
            EditTopAppBar(
                text = uiState.text,
                uiAction = viewModel::onUiAction
            )
        },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                EditTextField(
                    text = uiState.text,
                    uiAction = viewModel::onUiAction
                )
                EditImportance(
                    importance = uiState.importance,
                    uiAction = viewModel::onUiAction
                )
                EditDivider(padding = PaddingValues(horizontal = 16.dp))
                EditDeadline(
                    deadline = uiState.deadline,
                    isDateVisible = uiState.isDeadlineSet,
                    uiAction = viewModel::onUiAction
                )
                EditDivider(padding = PaddingValues(top = 16.dp, bottom = 8.dp))
                EditDelete(
                    enabled = uiState.isDeleteEnabled,
                    uiAction = viewModel::onUiAction
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewEditScreen(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        EditScreen(rememberNavController())
    }
}
