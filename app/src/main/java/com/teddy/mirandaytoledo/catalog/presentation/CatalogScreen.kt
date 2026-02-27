package com.teddy.mirandaytoledo.catalog.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.FilterFrames
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.catalog.presentation.components.CatalogChooser

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    onNavigateToScholarCatalog: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.catalog_chooser_school,
            icon = Icons.Outlined.School,
            onClick = onNavigateToScholarCatalog
        )
        CatalogChooser(modifier = Modifier.weight(weight = 1f), textResId = R.string.catalog_chooser_frames, icon = Icons.Outlined.FilterFrames)
        CatalogChooser(modifier = Modifier.weight(weight = 1f), textResId = R.string.catalog_chooser_prices, icon = Icons.Outlined.AttachMoney)
    }
}

@PreviewDynamicColors
@Composable
fun PreviewCatalogScreen() {
    CatalogScreen()
}