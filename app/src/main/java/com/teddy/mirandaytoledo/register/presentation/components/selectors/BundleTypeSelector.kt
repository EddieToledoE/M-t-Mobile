package com.teddy.mirandaytoledo.register.presentation.components.selectors

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.R

@Composable
fun BundleTypeSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = listOf("Bundle", "Single", "Just Photos"),
        onCategorySelected = {},
        label = R.string.bundleSelector_type_title
    )
    CategorySelector(
        categories = listOf("Resin", "Textured", "Glossy"),
        onCategorySelected = {},
        label = R.string.bundleSelector_surface_title
    )
    CategorySelector(
        categories = listOf("Small", "Medium", "Large"),
        onCategorySelected = {},
        label = R.string.bundleSelector_size_title
    )
    CategorySelector(
        categories = listOf("Drop", "Flag", "Butterfly"),
        onCategorySelected = {},
        label = R.string.bundleSelector_model_title
    )
    CategorySelector(
        categories = listOf("Chocolate", "Black", "Green"),
        onCategorySelected = {},
        label = R.string.bundleSelector_color_title
    )
}