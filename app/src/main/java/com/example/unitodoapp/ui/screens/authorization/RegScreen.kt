package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.components.authorization.RegistrationTextField
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun RegScreen(
    navController: NavController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    AuthContainer(
        screenType = Screen.REG,
        buttonText = "Create Account",
        onButtonClick = { /*TODO*/ },
        bottomSuggestText = "Already have an account? Log In",
        onBottomTextClick = { /*TODO*/ }
    ) {
        Text(
            text = "Create Account",
            fontSize = 24.sp,
            color = ExtendedTheme.colors.labelPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        RegistrationTextField(
            text = "",
            labelText = "Enter login",
            onValueChange = { }
        )

        RegistrationTextField(
            text = "",
            labelText = "Enter password",
            isPassword = true,
            onValueChange = { }
        )

        RegistrationTextField(
            text = "",
            labelText = "Confirm password",
            isPassword = true,
            onValueChange = { }
        )

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewRegistration(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        RegScreen(rememberNavController())
    }
}
