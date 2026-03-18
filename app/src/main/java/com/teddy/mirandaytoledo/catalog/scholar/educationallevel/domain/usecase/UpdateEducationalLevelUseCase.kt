package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class UpdateEducationalLevelUseCase(
    private val repository: EducationalLevelRepository
) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        maxGrade: Int,
        isActive: Boolean
    ): Result<EducationalLevel, NetworkError> {
        return repository.update(id = id, name = name, maxGrade = maxGrade, isActive = isActive)
    }
}
