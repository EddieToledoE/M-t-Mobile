package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.mappers

import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto.FrameModelFinishColorRelationDto
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto.FrameModelFinishRelationDto
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation

fun FrameModelFinishRelationDto.toDomain(): FrameModelFinishRelation {
    return FrameModelFinishRelation(
        frameModelId = frameModelId,
        frameModelName = frameModelName,
        finishId = finishId,
        finishName = finishName
    )
}

fun FrameModelFinishColorRelationDto.toDomain(): FrameModelFinishColorRelation {
    return FrameModelFinishColorRelation(
        frameModelId = frameModelId,
        frameModelName = frameModelName,
        finishId = finishId,
        finishName = finishName,
        colorId = colorId,
        colorName = colorName,
        colorHex = colorHex
    )
}
