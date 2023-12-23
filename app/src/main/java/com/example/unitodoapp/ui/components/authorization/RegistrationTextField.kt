package com.example.unitodoapp.ui.components.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitodoapp.ui.components.VisibilityIcon
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.Red
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun RegistrationTextField(
    value: String,
    labelText: String,
    isValid: Boolean = true,
    invalidMassage: String = "",
    isPassword: Boolean = false,
    isPassVisible: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onVisibilityClick: () -> Unit = {}
) {
    
    val labelColor = if (isValid)
        ExtendedTheme.colors.labelSecondary
    else
        Red

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(text = labelText, color = labelColor)
        },
        visualTransformation = if (isPassword && !isPassVisible)
            PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = if (isPassword)
            KeyboardOptions(keyboardType = KeyboardType.Password)
        else
            KeyboardOptions.Default,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ExtendedTheme.colors.backElevated,
            unfocusedContainerColor = ExtendedTheme.colors.backElevated,
            focusedBorderColor = Blue,
            unfocusedBorderColor = labelColor
        ),
        trailingIcon = {
            if (isPassword)
                VisibilityIcon(
                    isVisibleOff = isPassVisible,
                    onClick = { onVisibilityClick() },
                    color = ExtendedTheme.colors.labelSecondary
                )
        },
        singleLine = true,
        supportingText = {
            if (!isValid) {
                Text(
                    text = invalidMassage,
                    color = Red,
                    fontSize = 16.sp,
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyRegistrationTextField(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        Box(Modifier.background(ExtendedTheme.colors.backPrimary)) {
            Box(Modifier.padding(10.dp)) {
                RegistrationTextField("", "enter something")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewValidRegistrationTextField(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        Box(Modifier.background(ExtendedTheme.colors.backPrimary)) {
            Box(Modifier.padding(10.dp)) {
                RegistrationTextField("testLogin", "enter login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInvalidRegistrationTextField(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        Box(Modifier.background(ExtendedTheme.colors.backPrimary)) {
            Box(Modifier.padding(10.dp)) {
                RegistrationTextField(
                    "invalidLogin",
                    "enter login",
                    false,
                    "incorrect")
            }
        }
    }
}