package com.teddy.mirandaytoledo.catalog.frames.colors.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCatalogColorRequest(
    val name: String,
    val hex: String?
)
