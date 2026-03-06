package com.teddy.mirandaytoledo.catalog.domain.usecase

import com.teddy.mirandaytoledo.catalog.domain.School
import com.teddy.mirandaytoledo.catalog.domain.SchoolRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetSchoolsByLevelUseCase(private val repository: SchoolRepository) {
    suspend operator fun invoke(
        educationalLevelId: Int,
        page: Int,
        pageSize: Int,
        search: String? = null
    ): Result<List<School>, NetworkError> {
        return repository.getSchoolsByLevel(
            educationalLevelId = educationalLevelId,
            page = page,
            pageSize = pageSize,
            search = search
        )
    }
}
