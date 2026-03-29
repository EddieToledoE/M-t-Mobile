package com.teddy.mirandaytoledo.catalog.frames.finishes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFinishRequest(
    val name: String,
    val isActive: Boolean
)
