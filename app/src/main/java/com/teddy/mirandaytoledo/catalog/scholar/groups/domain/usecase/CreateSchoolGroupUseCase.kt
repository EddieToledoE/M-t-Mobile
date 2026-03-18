package com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateSchoolGroupUseCase(private val repository: SchoolGroupRepository) {
    suspend operator fun invoke(
        schoolId: Int,
        groupCode: String
    ): Result<SchoolGroup, NetworkError> {
        return repository.create(schoolId = schoolId, groupCode = groupCode)
    }
}
