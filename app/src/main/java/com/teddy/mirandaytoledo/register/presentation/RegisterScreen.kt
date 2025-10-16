package com.teddy.mirandaytoledo.register.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.core.navigation.Navigator
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold
import com.teddy.mirandaytoledo.register.presentation.components.RegisterStatus
import com.teddy.mirandaytoledo.register.presentation.components.SchoolSelector
import com.teddy.mirandaytoledo.ui.theme.MirandaytoledoTheme

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 10.dp)
            .border(
                width = 3.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), shape = RoundedCornerShape(
                    size = 16.dp
                )
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ) {
        RegisterStatus()
        Text(
            modifier = modifier.padding(all = 8.dp),
            text = "Informacion de la escuela",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(modifier = modifier.padding(horizontal = 8.dp), text = "Todos los campos son obligatorios", color = MaterialTheme.colorScheme.secondary)
        Column {
            SchoolSelector()
        }
    }
}

@Composable
@PreviewLightDark
fun RegisterScreenPreview() {
    MirandaytoledoTheme {
        MainScaffold({}, Navigator()) {
            RegisterScreen()
        }
    }
}