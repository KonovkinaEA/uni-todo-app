package com.example.unitodoapp.ui.edit

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
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun EditScreen(navController: NavHostController) {
    val editViewModel: EditViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate(List.route) {
                    popUpTo(List.route) { inclusive = true }
                }
            }
        ) {
            Text(text = "To ListScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditScreen() {
    TodoAppTheme(darkTheme = false) {
        EditScreen(rememberNavController())
    }
}
