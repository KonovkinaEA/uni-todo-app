package com.example.unitodoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unitodoapp.data.navigation.Edit
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.data.navigation.Settings
import com.example.unitodoapp.ui.screens.edit.EditScreen
import com.example.unitodoapp.ui.screens.list.ListScreen
import com.example.unitodoapp.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = List.route
    ) {
        composable(List.route) {
            ListScreen(navController = navController)
        }
        composable(Edit.route) {
            EditScreen(
                navController = navController
            )
        }
        composable(Edit.routeWithArgs, arguments = Edit.arguments) {
            EditScreen(
                navController = navController
            )
        }
        composable(Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
