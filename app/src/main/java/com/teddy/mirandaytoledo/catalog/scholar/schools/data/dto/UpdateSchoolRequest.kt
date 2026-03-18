package com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSchoolRequest(
    val name: String,
    val isActive: Boolean
)
