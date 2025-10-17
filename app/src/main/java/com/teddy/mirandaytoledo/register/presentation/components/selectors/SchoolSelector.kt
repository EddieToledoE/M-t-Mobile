package com.teddy.mirandaytoledo.register.presentation.components.selectors

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.teddy.mirandaytoledo.R

val options = listOf("Option A", "Option B", "Option C")

@Composable
fun SchoolSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = options,
        onCategorySelected = {},
        label = R.string.SchoolSelector_title_textfield
    )
}

@PreviewLightDark
@Composable
fun SchoolSelectorPreview(modifier: Modifier = Modifier) {
    SchoolSelector()
}