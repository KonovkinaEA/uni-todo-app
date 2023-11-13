package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun AuthScreen(
    navController: NavController,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    AuthContainer(
        screenType = Screen.WELCOME,
        buttonText = "Create Account",
        onButtonClick = { /*TODO*/ },
        bottomSuggestText = "Forgot password ?",
        onBottomTextClick = { /*TODO*/ }
    ) {
        Text(
            text = "Welcome to app",
            fontSize = 24.sp,
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            text = "Let's plan your day",
            fontSize = 14.sp,
            color = ExtendedTheme.colors.labelPrimary
        )

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewAuthorization(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        AuthScreen(rememberNavController())
    }
}
