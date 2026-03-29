package com.teddy.mirandaytoledo.catalog.frames.finishes.data.mappers

import com.teddy.mirandaytoledo.catalog.frames.finishes.data.dto.FinishDto
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish

fun FinishDto.toDomain(): Finish {
    return Finish(
        id = id,
        name = name,
        isActive = isActive
    )
}
