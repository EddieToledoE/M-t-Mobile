package com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColorRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateCatalogColorUseCase(
    private val repository: CatalogColorRepository
) {
    suspend operator fun invoke(name: String, hex: String?): Result<CatalogColor, NetworkError> {
        return repository.create(name = name, hex = hex)
    }
}
