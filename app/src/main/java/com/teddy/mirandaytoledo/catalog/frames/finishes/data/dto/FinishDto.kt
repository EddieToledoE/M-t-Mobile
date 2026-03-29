package com.teddy.mirandaytoledo.catalog.frames.finishes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FinishDto(
    val id: Int,
    val name: String,
    val isActive: Boolean
)
