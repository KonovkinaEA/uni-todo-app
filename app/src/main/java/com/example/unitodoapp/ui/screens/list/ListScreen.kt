package com.example.unitodoapp.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.data.navigation.Edit
import com.example.unitodoapp.data.navigation.Settings
import com.example.unitodoapp.ui.components.list.ListToDoes
import com.example.unitodoapp.ui.components.list.ListUiEventHandler
import com.example.unitodoapp.ui.screens.edit.actions.ListUiAction
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.ui.theme.White

@Composable
fun ListScreen(navController: NavHostController) {
    val viewModel: ListViewModel = hiltViewModel()


    ListUiEventHandler(
        uiEvent = viewModel.uiEvent,
        onNavigateToEditScreen = {
            navController.navigate(Edit.route)
        },
        onNavigateToSettingsScreen = {
            navController.navigate(Settings.route)
        }
    )

    Scaffold(
        topBar = { },
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

            Button(
                onClick = { navController.navigate(Settings.route) }
            ) {
                Text(text = "To SettingsScreen")
            }
            ListToDoes(
                //toDoes = TODO viewModel.getTodoItems(),
                onCheckboxClick = { todo ->
                    viewModel.onUiAction(ListUiAction.UpdateTodoItem(todo))
                },
                onItemClick = { todo ->
                    viewModel.onUiAction(ListUiAction.EditTodoItem(todo))
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
