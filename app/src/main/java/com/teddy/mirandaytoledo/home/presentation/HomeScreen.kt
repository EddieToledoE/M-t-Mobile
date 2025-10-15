package com.teddy.mirandaytoledo.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.home.presentation.components.HomeTitleText
import com.teddy.mirandaytoledo.home.presentation.components.TopBar
import com.teddy.mirandaytoledo.ui.theme.MirandaytoledoTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeTitleText(
            textResId = R.string.home_calendar_title
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .size(height = 300.dp, width = 0.dp)
                .background(Color.DarkGray)
        ) { }
        HomeTitleText(
            textResId = R.string.home_work_title
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .size(height = 300.dp, width = 0.dp)
                .background(Color.DarkGray)
        ) { }
        HomeTitleText(
            textResId = R.string.home_money_title
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .size(height = 300.dp, width = 0.dp)
                .background(Color.DarkGray)
        ) { }
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .height(8.dp), thickness = 8.dp
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .size(height = 100.dp, width = 0.dp)
                .background(Color.DarkGray)
        ) { }
    }
}

@Preview
@Composable
fun HomeScreenPrev() {
    MirandaytoledoTheme {
        HomeScreen()
    }
}
