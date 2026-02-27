package com.teddy.mirandaytoledo.catalog.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBusiness
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.R

@Composable
fun ScholarCatalogChooser(
    modifier: Modifier = Modifier,
    onNavigateToEducationalLevel: () -> Unit = {}
) {
        Column(modifier = modifier.fillMaxSize()) {
            CatalogChooser(
                modifier = Modifier.weight(weight = 1f),
                textResId = R.string.scholar_catalog_chooser_educational_level,
                icon = Icons.Outlined.Leaderboard,
                onClick = onNavigateToEducationalLevel
            )
            CatalogChooser(modifier = Modifier.weight(weight = 1f), textResId = R.string.scholar_catalog_chooser_schools, icon = Icons.Outlined.AddBusiness)
            CatalogChooser(modifier = Modifier.weight(weight = 1f), textResId = R.string.scholar_catalog_chooser_groups, icon = Icons.Outlined.Groups)
        }
    }