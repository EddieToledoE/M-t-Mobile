package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetSchoolGroupsUseCase(private val repository: SchoolGroupRepository) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        search: String? = null
    ): Result<List<SchoolGroup>, NetworkError> {
        return repository.getSchoolGroups(page = page, pageSize = pageSize, search = search)
    }
}
