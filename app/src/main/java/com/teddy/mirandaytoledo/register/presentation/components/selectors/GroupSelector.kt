package com.teddy.mirandaytoledo.register.presentation.components.selectors

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teddy.mirandaytoledo.R

val groups = listOf("A", "B", "C", "D", "E", "F", "G")
val grades = listOf("3", "6")


@Composable
fun GroupSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = groups,
        onCategorySelected = {},
        label = R.string.register_group_text,
        showArgumentText = false
    )
}

@Composable
fun GradeSelector(modifier: Modifier = Modifier) {
    CategorySelector(
        categories = grades,
        onCategorySelected = {},
        label = R.string.register_grade_text,
        showArgumentText = false
    )
}