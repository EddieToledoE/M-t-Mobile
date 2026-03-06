package com.teddy.mirandaytoledo.catalog.data.repository

import com.teddy.mirandaytoledo.catalog.data.dto.CreateSchoolGroupRequest
import com.teddy.mirandaytoledo.catalog.data.dto.UpdateSchoolGroupRequest
import com.teddy.mirandaytoledo.catalog.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.data.networking.SchoolGroupRemoteDataSource
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.domain.SchoolGroupRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class SchoolGroupRepositoryImpl(
    private val remoteDataSource: SchoolGroupRemoteDataSource
) : SchoolGroupRepository {

    override suspend fun getSchoolGroups(
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<SchoolGroup>, NetworkError> {
        return remoteDataSource.getSchoolGroups(page = page, pageSize = pageSize, search = search)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getSchoolGroupsBySchool(
        schoolId: Int,
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<SchoolGroup>, NetworkError> {
        return remoteDataSource.getSchoolGroupsBySchool(
            schoolId = schoolId,
            page = page,
            pageSize = pageSize,
            search = search
        ).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun create(schoolId: Int, groupCode: String): Result<SchoolGroup, NetworkError> {
        return remoteDataSource.create(
            CreateSchoolGroupRequest(schoolId = schoolId, groupCode = groupCode)
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        groupCode: String,
        isActive: Boolean
    ): Result<SchoolGroup, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateSchoolGroupRequest(groupCode = groupCode, isActive = isActive)
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
