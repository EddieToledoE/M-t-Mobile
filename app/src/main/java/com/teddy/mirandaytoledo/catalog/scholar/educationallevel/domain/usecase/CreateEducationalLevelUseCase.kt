package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateEducationalLevelUseCase(
    private val repository: EducationalLevelRepository
) {
    suspend operator fun invoke(name: String, maxGrade: Int): Result<EducationalLevel, NetworkError> {
        return repository.create(name = name, maxGrade = maxGrade)
    }
}
