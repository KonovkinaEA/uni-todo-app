package com.example.unitodoapp.ui.components.authorization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.White

@Composable
fun AuthorizationButton(
    text: String,
    containerColor: Color = White,
    contentColor: Color = Blue,
    borderColor: Color = Blue,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(width = 1.dp, color = borderColor),
        modifier = Modifier.width(250.dp).height(50.dp)
    ) {
        Text(text = text)
    }
}