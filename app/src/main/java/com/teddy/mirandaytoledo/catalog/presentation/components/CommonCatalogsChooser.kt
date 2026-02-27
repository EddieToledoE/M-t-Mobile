package com.teddy.mirandaytoledo.catalog.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CatalogChooser(
    modifier: Modifier = Modifier,
    @StringRes textResId: Int,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.background).border(width = 2.dp, color = MaterialTheme.colorScheme.primary).padding(all = 8.dp).clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Icon(imageVector = icon, contentDescription = "Scholar Catalogs", tint = MaterialTheme.colorScheme.primary )
        Text(text = stringResource(textResId), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
    }
}
