package com.teddy.mirandaytoledo.catalog.prices.producttypes.data.mappers

import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.ProductTypeDto
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType

fun ProductTypeDto.toDomain(): ProductType {
    return ProductType(
        id = id,
        name = name,
        requiresSize = requiresSize,
        requiresFinish = requiresFinish,
        requiresFrameModel = requiresFrameModel,
        requiresColor = requiresColor,
        allowedSizeGroup = allowedSizeGroup,
        isActive = isActive
    )
}
