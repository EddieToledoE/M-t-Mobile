package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetEducationalLevelsUseCase(
    private val repository: EducationalLevelRepository
) {
    suspend operator fun invoke(): Result<List<EducationalLevel>, NetworkError> {
        return repository.getAll()
    }
}
