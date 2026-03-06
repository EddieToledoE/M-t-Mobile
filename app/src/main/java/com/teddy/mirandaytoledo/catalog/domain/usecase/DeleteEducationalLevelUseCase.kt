package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteEducationalLevelUseCase(
    private val repository: EducationalLevelRepository
) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
