package com.teddy.mirandaytoledo.catalog.domain

data class EducationalLevel(
    val id: Int,
    val name: String,
    val maxGrade: Int,
    val isActive: Boolean
)
