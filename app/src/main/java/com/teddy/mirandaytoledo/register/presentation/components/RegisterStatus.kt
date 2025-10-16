package com.teddy.mirandaytoledo.register.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teddy.mirandaytoledo.ui.theme.MirandaytoledoTheme

@Composable
fun RegisterStatus(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(40.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "1",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        HorizontalDivider(
            modifier = modifier
                .width(60.dp)
                .padding(top = 20.dp)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                ),
            color = MaterialTheme.colorScheme.primary,
            thickness = 6.dp
        )
        Box(
            modifier = modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(40.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "2",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        HorizontalDivider(
            modifier = modifier
                .width(60.dp)
                .padding(top = 20.dp)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                ),
            color = MaterialTheme.colorScheme.primary,
            thickness = 6.dp
        )
        Box(
            modifier = modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(40.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "3",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RegisterStatusPreview(modifier: Modifier = Modifier) {
    MirandaytoledoTheme {
        RegisterStatus()
    }
}