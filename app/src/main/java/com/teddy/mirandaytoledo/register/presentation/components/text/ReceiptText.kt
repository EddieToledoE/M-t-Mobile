package com.teddy.mirandaytoledo.register.presentation.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.ui.theme.bodyFontFamily
import com.teddy.mirandaytoledo.ui.theme.displayFontFamily

@Composable
fun ReceiptText(modifier: Modifier = Modifier, string: String) {
    Text(
        text = string,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = displayFontFamily,
        modifier = modifier.padding(horizontal = 10.dp, vertical = 3.dp)
    )
}