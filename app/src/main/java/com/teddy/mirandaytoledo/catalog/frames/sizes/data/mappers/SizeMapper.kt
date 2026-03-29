package com.teddy.mirandaytoledo.catalog.frames.sizes.data.mappers

import com.teddy.mirandaytoledo.catalog.frames.sizes.data.dto.SizeDto
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size

fun SizeDto.toDomain(): Size {
    return Size(
        id = id,
        name = name,
        sizeGroup = sizeGroup,
        isActive = isActive
    )
}
