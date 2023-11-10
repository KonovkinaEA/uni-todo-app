package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.components.authorization.AuthorizationButton
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.ui.theme.White

@Composable
fun AuthorizationScreen(
    navController: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(ExtendedTheme.colors.backPrimary)
            .padding(top = 20.dp, bottom = 70.dp)
            .padding(horizontal = 40.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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

        AuthorizationButton(
            text = "Create Account",
            contentColor = White,
            containerColor = Blue,
            onClick = { }
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthorizationButton(
            text = "Login",
            containerColor = White,
            onClick = { }
        )

        TextButton(
            onClick = { }
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                text = "Forgot password ?",
                color = ExtendedTheme.colors.labelTertiary,
                textAlign = TextAlign.End,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewAuthorization(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        AuthorizationScreen(rememberNavController())
    }
}
