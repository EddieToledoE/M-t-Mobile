package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain

data class EducationalLevel(
    val id: Int,
    val name: String,
    val maxGrade: Int,
    val isActive: Boolean
)
