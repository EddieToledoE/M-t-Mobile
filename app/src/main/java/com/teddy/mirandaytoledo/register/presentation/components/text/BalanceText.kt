package com.teddy.mirandaytoledo.register.presentation.components.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.ui.theme.displayFontFamily

@Composable
fun BalanceText(
    modifier: Modifier = Modifier,
    value: String,
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 3.dp),
        text = value,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        fontFamily = displayFontFamily,
        color = MaterialTheme.colorScheme.primary
    )
}