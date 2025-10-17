package com.teddy.mirandaytoledo.register.presentation.components.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArgumentText(modifier: Modifier = Modifier, @StringRes label: Int) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp, horizontal = 10.dp),
        text = stringResource(label),
        textAlign = TextAlign.Start,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}