package com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository

class DeleteSizeUseCase(
    private val repository: SizeRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}
