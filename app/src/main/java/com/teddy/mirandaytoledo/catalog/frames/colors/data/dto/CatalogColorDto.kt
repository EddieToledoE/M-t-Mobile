package com.teddy.mirandaytoledo.catalog.frames.colors.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CatalogColorDto(
    val id: Int,
    val name: String,
    val hex: String?,
    val isActive: Boolean
)
