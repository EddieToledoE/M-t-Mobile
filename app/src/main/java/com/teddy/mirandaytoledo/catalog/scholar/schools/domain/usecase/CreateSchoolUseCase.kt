package com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.SchoolRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateSchoolUseCase(private val repository: SchoolRepository) {
    suspend operator fun invoke(
        educationalLevelId: Int,
        name: String
    ): Result<School, NetworkError> {
        return repository.create(educationalLevelId = educationalLevelId, name = name)
    }
}
