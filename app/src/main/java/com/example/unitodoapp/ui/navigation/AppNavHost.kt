package com.example.unitodoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unitodoapp.data.navigation.AUTH_ROOT
import com.example.unitodoapp.data.navigation.Edit
import com.example.unitodoapp.data.navigation.List
import com.example.unitodoapp.data.navigation.Settings
import com.example.unitodoapp.data.navigation.launchNavAuth
import com.example.unitodoapp.ui.screens.edit.EditScreen
import com.example.unitodoapp.ui.screens.list.ListScreen
import com.example.unitodoapp.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    isUserLogged: Boolean
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (isUserLogged) List.route else AUTH_ROOT
    ) {

        launchNavAuth(navController = navController)

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
