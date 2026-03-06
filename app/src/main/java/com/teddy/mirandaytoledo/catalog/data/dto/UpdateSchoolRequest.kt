package com.teddy.mirandaytoledo.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSchoolRequest(
    val name: String,
    val isActive: Boolean
)
