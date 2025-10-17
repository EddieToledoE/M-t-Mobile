package com.teddy.mirandaytoledo.register.presentation.components.selectors


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.teddy.mirandaytoledo.R

val EducationalLevelOptions = listOf("Kinder Garden", "Primary", "High School")

@Composable
fun EducationalLevelSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = EducationalLevelOptions,
        onCategorySelected = {},
        label = R.string.EducationalLevel_title_textfield
    )
}

@PreviewLightDark
@Composable
fun EducationalLevelSelectorPreview(modifier: Modifier = Modifier) {
    EducationalLevelSelector()
}