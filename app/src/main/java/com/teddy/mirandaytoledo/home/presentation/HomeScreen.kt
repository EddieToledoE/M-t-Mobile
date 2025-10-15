package com.teddy.mirandaytoledo.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teddy.mirandaytoledo.home.presentation.components.TopBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopBar(modifier = modifier) }
    )
    { paddingValues ->
        Column(
            modifier = modifier
                .background(Color.White)
                .padding(paddingValues)
        )
        {

        }
    }
}