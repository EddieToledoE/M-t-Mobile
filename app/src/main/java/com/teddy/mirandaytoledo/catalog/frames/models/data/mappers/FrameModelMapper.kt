package com.teddy.mirandaytoledo.catalog.frames.models.data.mappers

import com.teddy.mirandaytoledo.catalog.frames.models.data.dto.FrameModelDto
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel

fun FrameModelDto.toDomain(): FrameModel {
    return FrameModel(
        id = id,
        name = name,
        isActive = isActive
    )
}
