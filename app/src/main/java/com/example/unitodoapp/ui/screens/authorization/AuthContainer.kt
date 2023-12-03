package com.example.unitodoapp.ui.screens.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.components.authorization.AuthButton
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.ui.theme.White

@Composable
fun AuthContainer(
    screenType: Screen,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit = {},
    bottomSuggestText: String,
    onBottomTextClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
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
            content()
        }
        AuthButton(
            text = buttonText,
            onClick = {
                onButtonClick()
                      },
            contentColor = White,
            containerColor = Blue
        )
        if (screenType == Screen.WELCOME) {
            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(
                text = stringResource(R.string.welcome_login_button_text),
                containerColor = White,
                onClick = { onSecondButtonClick() }
            )
        }

        TextButton(
            onClick = {
                onBottomTextClick()
            }
        ) {
            Text(
                text = bottomSuggestText,
                modifier = Modifier.fillMaxWidth(),
                color = ExtendedTheme.colors.labelTertiary,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }
    }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewAuthorizationHostStart(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        AuthContainer(
            Screen.WELCOME,
            "Button text",
            {},
            bottomSuggestText = "bottomSuggestText",
            onBottomTextClick = {}
        ) {

        }
    }
}
