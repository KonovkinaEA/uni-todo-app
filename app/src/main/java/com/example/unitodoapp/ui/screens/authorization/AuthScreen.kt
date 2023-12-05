package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.R
import com.example.unitodoapp.data.navigation.LogIn
import com.example.unitodoapp.data.navigation.PassRec
import com.example.unitodoapp.data.navigation.Reg
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun AuthScreen(
    navController: NavController,
) {
    AuthContainer(
        screenType = Screen.WELCOME,
        buttonText = stringResource(R.string.welcome_button_text_create_account),
        onButtonClick = { navController.navigate(Reg.route) },
        onSecondButtonClick = { navController.navigate((LogIn.route)) },
        bottomSuggestText = stringResource(R.string.welcome_bottom_text),
        onBottomTextClick = { navController.navigate((PassRec.route)) }
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            fontSize = 24.sp,
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(R.string.welcome_support_headline_text),
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
