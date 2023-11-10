package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.components.authorization.AuthorizationButton
import com.example.unitodoapp.ui.components.authorization.RegistrationTextField
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.ui.theme.White

@Composable
fun RegistrationScreen(
    navController: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(ExtendedTheme.colors.backPrimary)
            .padding(top = 120.dp, bottom = 70.dp)
            .padding(horizontal = 40.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
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
                text = "some password",
                labelText = "Enter password",
                isPassword = true,
                onValueChange = { }
            )

            RegistrationTextField(
                text = "some password",
                labelText = "Confirm password",
                isPassword = true,
                onValueChange = { }
            )
        }





        AuthorizationButton(
            text = "Create Account",
            contentColor = White,
            containerColor = Blue,
            onClick = { }
        )

        TextButton(
            onClick = { }
        ) {
            Text(
                modifier = Modifier,
                text = "Already have an account? Sign in",
                color = ExtendedTheme.colors.labelTertiary,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewRegistration(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        RegistrationScreen(rememberNavController())
    }
}
