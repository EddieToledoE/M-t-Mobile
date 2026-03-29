package com.teddy.mirandaytoledo.catalog.frames.colors.data.mappers

import com.teddy.mirandaytoledo.catalog.frames.colors.data.dto.CatalogColorDto
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor

fun CatalogColorDto.toDomain(): CatalogColor {
    return CatalogColor(
        id = id,
        name = name,
        hex = hex,
        isActive = isActive
    )
}
