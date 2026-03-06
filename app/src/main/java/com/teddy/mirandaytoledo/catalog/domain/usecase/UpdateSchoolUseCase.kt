package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.School
import com.teddy.mirandaytoledo.catalog.domain.SchoolRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class UpdateSchoolUseCase(private val repository: SchoolRepository) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        isActive: Boolean
    ): Result<School, NetworkError> {
        return repository.update(id = id, name = name, isActive = isActive)
    }
}
