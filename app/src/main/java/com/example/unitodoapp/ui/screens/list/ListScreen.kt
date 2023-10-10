package com.example.unitodoapp.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
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
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun ListScreen(navController: NavHostController) {
    val viewModel: ListViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate(Edit.route) }
        ) {
            Text(text = "To EditScreen")
        }
        Button(
            onClick = { navController.navigate(Settings.route) }
        ) {
            Text(text = "To SettingsScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListScreen() {
    TodoAppTheme(darkTheme = false) {
        ListScreen(rememberNavController())
    }
}
