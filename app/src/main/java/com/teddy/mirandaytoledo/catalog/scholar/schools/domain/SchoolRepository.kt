package com.teddy.mirandaytoledo.catalog.scholar.schools.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface SchoolRepository {
    suspend fun getSchools(
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<School>, NetworkError>

    suspend fun getSchoolsByLevel(
        educationalLevelId: Int,
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<School>, NetworkError>

    suspend fun create(
        educationalLevelId: Int,
        name: String
    ): Result<School, NetworkError>

    suspend fun update(
        id: Int,
        name: String,
        isActive: Boolean
    ): Result<School, NetworkError>

    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
