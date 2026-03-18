package com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.SchoolRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteSchoolUseCase(private val repository: SchoolRepository) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
