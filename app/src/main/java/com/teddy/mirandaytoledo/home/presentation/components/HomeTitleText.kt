package com.teddy.mirandaytoledo.home.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.R

@Composable
fun HomeTitleText(
    modifier: Modifier = Modifier,
    @StringRes textResId: Int,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp),
        text = stringResource(textResId),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        color = Color(0xFFFFDD00)
    )
}