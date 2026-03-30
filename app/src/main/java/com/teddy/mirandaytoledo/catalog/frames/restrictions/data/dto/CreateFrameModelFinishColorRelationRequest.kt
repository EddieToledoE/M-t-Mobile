package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateFrameModelFinishColorRelationRequest(
    val frameModelId: Int,
    val finishId: Int,
    val colorId: Int
)
