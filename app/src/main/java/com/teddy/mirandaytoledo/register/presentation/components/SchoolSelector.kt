package com.teddy.mirandaytoledo.register.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

val options = listOf("Option A", "Option B", "Option C")

@Composable
fun SchoolSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = options,
        onCategorySelected = {}
    )
}

@PreviewLightDark
@Composable
fun SchoolSelectorPreview(modifier: Modifier = Modifier) {
    SchoolSelector()
}