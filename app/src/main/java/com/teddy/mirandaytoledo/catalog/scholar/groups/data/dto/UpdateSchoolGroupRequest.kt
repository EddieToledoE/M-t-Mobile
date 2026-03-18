package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSchoolGroupRequest(
    val groupCode: String,
    val isActive: Boolean
)
