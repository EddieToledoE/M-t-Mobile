package com.teddy.mirandaytoledo.catalog.frames.models.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FrameModelDto(
    val id: Int,
    val name: String,
    val isActive: Boolean
)
