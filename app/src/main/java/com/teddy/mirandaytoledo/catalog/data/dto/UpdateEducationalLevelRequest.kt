package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateEducationalLevelRequest(
    val name: String,
    val maxGrade: Int,
    val displayOrder: Int = 1,
    val isActive: Boolean
)
