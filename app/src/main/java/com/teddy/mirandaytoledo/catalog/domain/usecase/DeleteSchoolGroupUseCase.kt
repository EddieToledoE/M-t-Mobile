package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteSchoolGroupUseCase(private val repository: SchoolGroupRepository) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
