package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSchoolGroupRequest(
    val schoolId: Int,
    val groupCode: String
)
