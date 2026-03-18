package com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SchoolDto(
    val id: Int,
    val educationalLevelId: Int,
    val educationalLevelName: String?,
    val name: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)
