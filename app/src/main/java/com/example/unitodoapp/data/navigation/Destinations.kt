package com.example.unitodoapp.data.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
object Auth : Destination {
    override val route: String = "auth"
}

object Reg : Destination {
    override val route: String = "reg"
}

object LogIn: Destination {
    override val route: String = "login"
}

object PassRec : Destination {
    override val route: String = "passRec"
}

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
