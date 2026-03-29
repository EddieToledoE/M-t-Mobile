package com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository

class CreateSizeUseCase(
    private val repository: SizeRepository
) {
    suspend operator fun invoke(name: String, sizeGroup: String) = repository.create(
        name = name,
        sizeGroup = sizeGroup
    )
}
