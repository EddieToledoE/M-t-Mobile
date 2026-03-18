package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateEducationalLevelRequest(
    val name: String,
    val maxGrade: Int,
    val displayOrder: Int = 1
)