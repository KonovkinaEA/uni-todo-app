package com.example.unitodoapp.data.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.unitodoapp.ui.screens.authorization.AuthScreen
import com.example.unitodoapp.ui.screens.authorization.LogInScreen
import com.example.unitodoapp.ui.screens.authorization.PassRecoveryScreen
import com.example.unitodoapp.ui.screens.authorization.RegScreen

const val AUTH_ROOT = "authorization"

fun NavGraphBuilder.launchNavAuth(navController: NavController) {
    navigation(startDestination = Auth.route, route = AUTH_ROOT) {

        composable(Auth.route) {
            AuthScreen(navController = navController)
        }

        composable(Reg.route) {
            RegScreen(navController = navController)
        }

        composable(LogIn.route) {
            LogInScreen(navController = navController)
        }

        composable(PassRec.route) {
            PassRecoveryScreen(navController = navController)
        }
    }
}