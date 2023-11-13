package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.ui.components.authorization.RegistrationTextField
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun PassRecoveryScreen(
    navController: NavController
) {

    AuthContainer(
        screenType = Screen.PASSREC,
        buttonText = "Recover password",
        onButtonClick = { /*TODO*/ },
        bottomSuggestText = "Doesn't have an account? Create one",
        onBottomTextClick = { /*TODO*/ }
    ) {
        Text(
            text = "Password recovery",
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
            labelText = "Create new password",
            isPassword = true,
            onValueChange = { }
        )

        RegistrationTextField(
            text = "",
            labelText = "Confirm new password",
            isPassword = true,
            onValueChange = { }
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
