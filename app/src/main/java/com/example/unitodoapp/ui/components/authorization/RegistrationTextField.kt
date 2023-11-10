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
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun RegistrationTextField(
    text: String,
    labelText: String,
    isPassword: Boolean = false,
    onValueChange: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { },
        label = {
            Text(text = labelText, color = ExtendedTheme.colors.labelSecondary)
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = if (isPassword)
            KeyboardOptions(keyboardType = KeyboardType.Password)
        else
            KeyboardOptions.Default,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ExtendedTheme.colors.backElevated,
            unfocusedContainerColor = ExtendedTheme.colors.backElevated,
            focusedBorderColor = Blue
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationTextField(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        Box(Modifier.background(ExtendedTheme.colors.backPrimary)) {
            Box(Modifier.padding(10.dp)) {
                RegistrationTextField("", "enter something") {}
            }
        }
    }
}