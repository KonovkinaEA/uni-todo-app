package com.example.unitodoapp.data.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object List : Destination {
    override val route: String = "list"
}

object Edit : Destination {
    const val id = "id"
    override val route: String = "edit"

    const val routeWithArgs = "edit/{id}"

    val arguments = listOf(
        navArgument(id) {
            type = NavType.StringType
        }
    )

    fun navToOrderWithArgs(
        id: String = ""
    ): String {
        return "$route/$id"
    }
}

object Settings : Destination {
    override val route: String = "settings"
}
