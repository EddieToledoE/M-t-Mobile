package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateFrameModelFinishRelationRequest(
    val frameModelId: Int,
    val finishId: Int
)
