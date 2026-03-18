package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SchoolGroupDto(
    val id: Int,
    val schoolId: Int,
    val groupCode: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)
