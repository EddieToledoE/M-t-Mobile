package com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColorRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteCatalogColorUseCase(
    private val repository: CatalogColorRepository
) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
