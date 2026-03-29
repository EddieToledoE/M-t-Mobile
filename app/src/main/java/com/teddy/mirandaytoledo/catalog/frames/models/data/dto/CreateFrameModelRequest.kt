package com.teddy.mirandaytoledo.catalog.frames.models.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateFrameModelRequest(
    val name: String
)
