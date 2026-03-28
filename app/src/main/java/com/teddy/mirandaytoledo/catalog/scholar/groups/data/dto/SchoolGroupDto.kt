package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SchoolGroupDto(
    val id: Int,
    val schoolId: Int,
    val schoolName: String? = null,
    val educationalLevelId: Int? = null,
    val educationalLevelName: String? = null,
    val groupCode: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)
