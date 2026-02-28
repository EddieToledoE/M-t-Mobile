  package com.teddy.mirandaytoledo.catalog.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.core.presentation.components.MainScaffold

@Composable
fun EducationalLevelScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Educational Level Screen (CRUD here)")
    }
}
