package com.teddy.mirandaytoledo.catalog.scholar.schools.data.repository

import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.SchoolRepository
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto.CreateSchoolRequest
import com.teddy.mirandaytoledo.catalog.scholar.schools.data.dto.UpdateSchoolRequest
import com.teddy.mirandaytoledo.catalog.scholar.schools.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.scholar.schools.data.networking.SchoolRemoteDataSource
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class SchoolRepositoryImpl(
    private val remoteDataSource: SchoolRemoteDataSource
) : SchoolRepository {

    override suspend fun getSchools(
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<School>, NetworkError> {
        return remoteDataSource.getSchools(page = page, pageSize = pageSize, search = search)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getSchoolsByLevel(
        educationalLevelId: Int,
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<School>, NetworkError> {
        return remoteDataSource.getSchoolsByLevel(
            educationalLevelId = educationalLevelId,
            page = page,
            pageSize = pageSize,
            search = search
        ).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun create(
        educationalLevelId: Int,
        name: String
    ): Result<School, NetworkError> {
        return remoteDataSource.create(
            CreateSchoolRequest(educationalLevelId = educationalLevelId, name = name)
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        name: String,
        isActive: Boolean
    ): Result<School, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateSchoolRequest(name = name, isActive = isActive)
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}