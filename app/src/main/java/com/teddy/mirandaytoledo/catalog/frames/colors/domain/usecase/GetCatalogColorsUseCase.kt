package com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColorRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetCatalogColorsUseCase(
    private val repository: CatalogColorRepository
) {
    suspend operator fun invoke(): Result<List<CatalogColor>, NetworkError> {
        return repository.getAll()
    }
}
