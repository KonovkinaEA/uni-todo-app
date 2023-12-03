package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.navOptions
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.navigation.LogIn
import com.example.unitodoapp.data.navigation.PassRec
import com.example.unitodoapp.data.navigation.Reg
import com.example.unitodoapp.ui.components.authorization.RegistrationTextField
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiAction
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiEvent
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun PassRecoveryScreen(
    navController: NavController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                AuthUiEvent.NavigateToLog -> navController.navigate(
                    LogIn.route,
                    navOptions {
                        popUpTo(PassRec.route) { inclusive = true }
                    }
                )

                else -> {}
            }
        }
    }

    AuthContainer(
        screenType = Screen.PASSREC,
        buttonText = "Recover password",
        onButtonClick = {
            viewModel.onUiAction(
                AuthUiAction.UpdatePassForUser(
                    User(
                        email = uiState.email,
                        password = uiState.password
                    )
                )
            )
        },
        bottomSuggestText = "Doesn't have an account? Create one",
        onBottomTextClick = {
            navController.navigate(
                Reg.route,
                navOptions { popUpTo(PassRec.route) { inclusive = true } })
        }
    ) {
        Text(
            text = "Password recovery",
            fontSize = 24.sp,
            color = ExtendedTheme.colors.labelPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        RegistrationTextField(
            value = uiState.email,
            labelText = "Enter email",
            isValid = uiState.isEmailValid,
            invalidMassage = uiState.emailErrorMassage,
            onValueChange = { text ->
                viewModel.onUiAction(AuthUiAction.UpdateLogin(text))
            }
        )

        RegistrationTextField(
            value = uiState.password,
            labelText = "Enter new password",
            isValid = uiState.isPassValid,
            invalidMassage = uiState.passErrorMassage,
            isPassword = true,
            onValueChange = { text ->
                viewModel.onUiAction(AuthUiAction.UpdatePass(text))
            },
            isPassVisible = uiState.isPassVisible,
            onVisibilityClick = {
                viewModel.onUiAction(AuthUiAction.UpdatePassVisibility)
            }
        )

        RegistrationTextField(
            value = uiState.confPassword,
            labelText = "Confirm new password",
            isValid = (uiState.password == uiState.confPassword),
            invalidMassage = "passwords doesn't match",
            isPassword = true,
            onValueChange = { text ->
                viewModel.onUiAction(AuthUiAction.UpdateConfirmPass(text))
            },
            isPassVisible = uiState.isPassConfVisible,
            onVisibilityClick = {
                viewModel.onUiAction(AuthUiAction.UpdateConfPassVisibility)
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewPassRecovery(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        PassRecoveryScreen(rememberNavController())
    }
}
