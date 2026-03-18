package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EducationalLevelDto(
    val id: Int,
    val name: String,
    val maxGrade: Int,
    val displayOrder: Int,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)