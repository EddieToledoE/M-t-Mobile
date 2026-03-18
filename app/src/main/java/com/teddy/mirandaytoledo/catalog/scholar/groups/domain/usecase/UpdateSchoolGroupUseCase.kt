package com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class UpdateSchoolGroupUseCase(private val repository: SchoolGroupRepository) {
    suspend operator fun invoke(
        id: Int,
        groupCode: String,
        isActive: Boolean
    ): Result<SchoolGroup, NetworkError> {
        return repository.update(id = id, groupCode = groupCode, isActive = isActive)
    }
}
