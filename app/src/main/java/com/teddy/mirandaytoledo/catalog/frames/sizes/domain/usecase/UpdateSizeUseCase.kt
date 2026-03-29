package com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository

class UpdateSizeUseCase(
    private val repository: SizeRepository
) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        sizeGroup: String,
        isActive: Boolean
    ) = repository.update(
        id = id,
        name = name,
        sizeGroup = sizeGroup,
        isActive = isActive
    )
}
