package com.teddy.mirandaytoledo.catalog.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DesignServices
import androidx.compose.material.icons.outlined.FormatShapes
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.R

@Composable
fun FrameCatalogsChooser(
    modifier: Modifier = Modifier,
    onNavigateToFinishes: () -> Unit = {},
    onNavigateToSizes: () -> Unit = {},
    onNavigateToModels: () -> Unit = {},
    onNavigateToColors: () -> Unit = {},
    onNavigateToRestrictions: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.frame_catalog_chooser_finishes,
            icon = Icons.Outlined.DesignServices,
            onClick = onNavigateToFinishes
        )
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.frame_catalog_chooser_sizes,
            icon = Icons.Outlined.FormatShapes,
            onClick = onNavigateToSizes
        )
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.frame_catalog_chooser_models,
            icon = Icons.Outlined.Style,
            onClick = onNavigateToModels
        )
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.frame_catalog_chooser_colors,
            icon = Icons.Outlined.ColorLens,
            onClick = onNavigateToColors
        )
        CatalogChooser(
            modifier = Modifier.weight(weight = 1f),
            textResId = R.string.frame_catalog_chooser_restrictions,
            icon = Icons.Outlined.Warning,
            onClick = onNavigateToRestrictions
        )
    }
}
