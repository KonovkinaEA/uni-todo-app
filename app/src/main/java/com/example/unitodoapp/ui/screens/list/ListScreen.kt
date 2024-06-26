package com.example.unitodoapp.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.data.navigation.Edit
import com.example.unitodoapp.data.navigation.Settings
import com.example.unitodoapp.ui.components.list.ListToDoes
import com.example.unitodoapp.ui.components.list.ListTopAppBar
import com.example.unitodoapp.ui.screens.list.actions.ListUiAction
import com.example.unitodoapp.ui.screens.list.actions.ListUiEvent
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController) {
    val viewModel: ListViewModel = hiltViewModel()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                ListUiEvent.NavigateToNewTodoItem -> navController.navigate(Edit.route)
                ListUiEvent.NavigateToSettings -> navController.navigate(Settings.route)
                is ListUiEvent.NavigateToEditTodoItem -> navController.navigate(
                    Edit.navToOrderWithArgs(
                        it.id
                    )
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListTopAppBar(
                scrollBehavior = scrollBehavior,
                doneTasks = uiState.countDoneTasks,
                isFiltered = uiState.isFiltered,
                onSettingsClick = { navController.navigate(Settings.route) },
                onVisibilityClick = { isFiltered ->
                    viewModel.onUiAction(ListUiAction.ChangeFilter(isFiltered))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onUiAction(ListUiAction.AddNewItem) },
                shape = CircleShape,
                containerColor = Blue,
                contentColor = White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        },
        containerColor = ExtendedTheme.colors.backPrimary,
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ListToDoes(
                toDoes = uiState.todoItems,
                onItemClick = { todoItem -> viewModel.onUiAction(ListUiAction.EditTodoItem(todoItem)) },
                onDelete = { todoItem -> viewModel.onUiAction(ListUiAction.RemoveTodoItem(todoItem)) },
                onUpdate = { todoItem ->
                    viewModel.onUiAction(
                        ListUiAction.UpdateTodoItem(
                            todoItem.copy(
                                isDone = !todoItem.isDone
                            )
                        )
                    )
                }
            )
        }
    }


}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListScreen() {
    TodoAppTheme(darkTheme = false) {
        ListScreen(rememberNavController())
    }
}
