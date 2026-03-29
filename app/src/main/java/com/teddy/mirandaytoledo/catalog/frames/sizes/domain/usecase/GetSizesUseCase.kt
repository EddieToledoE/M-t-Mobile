package com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository

class GetSizesUseCase(
    private val repository: SizeRepository
) {
    suspend operator fun invoke() = repository.getAll()
}
