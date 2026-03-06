package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSchoolRequest(
    val educationalLevelId: Int,
    val name: String
)
