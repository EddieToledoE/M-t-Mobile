package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain

data class FrameModelFinishColorRelation(
    val frameModelId: Int,
    val frameModelName: String,
    val finishId: Int,
    val finishName: String,
    val colorId: Int,
    val colorName: String,
    val colorHex: String?
)
