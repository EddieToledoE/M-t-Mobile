package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FrameModelFinishColorRelationDto(
    val frameModelId: Int,
    val frameModelName: String,
    val finishId: Int,
    val finishName: String,
    val colorId: Int,
    val colorName: String,
    val colorHex: String?
)
