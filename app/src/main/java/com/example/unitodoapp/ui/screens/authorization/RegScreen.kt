package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.unitodoapp.R
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.navigation.LogIn
import com.example.unitodoapp.data.navigation.Reg
import com.example.unitodoapp.ui.components.authorization.RegistrationTextField
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiAction
import com.example.unitodoapp.ui.screens.authorization.actions.AuthUiEvent
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.utils.MAX_LOGIN_LEN
import com.example.unitodoapp.utils.MIN_LOGIN_LEN

@Composable
fun RegScreen(
    navController: NavController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                AuthUiEvent.NavigateToLog -> navController.navigate(LogIn.route,
                    navOptions {
                        popUpTo(Reg.route) { inclusive = true }
                    }
                )

                else -> {}
            }
        }
    }

    AuthContainer(
        screenType = Screen.REG,
        buttonText = stringResource(R.string.welcome_button_text_create_account),
        onButtonClick = {
            viewModel.onUiAction(
                AuthUiAction.RegisterNewUser(
                    User(
                        email = uiState.email,
                        password = uiState.password
                    )
                )
            )
        },
        bottomSuggestText = stringResource(R.string.reg_bottom_text),
        onBottomTextClick = {
            navController.navigate(LogIn.route)
        }
    ) {
        Text(
            text = stringResource(R.string.welcome_button_text_create_account),
            fontSize = 24.sp,
            color = ExtendedTheme.colors.labelPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        RegistrationTextField(
            value = uiState.email,
            labelText = stringResource(R.string.reg_field_label_email),
            isValid = uiState.isEmailValid,
            invalidMassage = if (uiState.isEmailOccupy) stringResource(R.string.error_text_occupied_email)
                else stringResource(R.string.error_text_invalid_email),
            onValueChange = { text ->
                viewModel.onUiAction(AuthUiAction.UpdateLogin(text))
            }
        )

        RegistrationTextField(
            value = uiState.password,
            labelText = stringResource(R.string.reg_field_label_pass),
            isValid = uiState.isPassValid,
            invalidMassage = stringResource(
                R.string.error_text_invalid_pass,
                MIN_LOGIN_LEN,
                MAX_LOGIN_LEN
            ),
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
            labelText = stringResource(R.string.reg_field_label_conf_pass),
            isValid = (uiState.password == uiState.confPassword),
            invalidMassage = stringResource(R.string.error_text_conf_pass),
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
fun PreviewRegistration(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        RegScreen(rememberNavController())
    }
}
