package com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase

import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.SchoolRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetSchoolsUseCase(private val repository: SchoolRepository) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        search: String? = null
    ): Result<List<School>, NetworkError> {
        return repository.getSchools(page = page, pageSize = pageSize, search = search)
    }
}
