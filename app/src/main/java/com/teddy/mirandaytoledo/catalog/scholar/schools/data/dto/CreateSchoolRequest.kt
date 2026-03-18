package com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSchoolRequest(
    val educationalLevelId: Int,
    val name: String
)
