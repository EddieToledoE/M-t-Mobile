package com.teddy.mirandaytoledo.catalog.frames.colors.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCatalogColorRequest(
    val name: String,
    val hex: String?
)
