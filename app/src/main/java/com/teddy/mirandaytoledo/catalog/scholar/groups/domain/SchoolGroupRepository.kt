package com.teddy.mirandaytoledo.catalog.scholar.groups.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface SchoolGroupRepository {
    suspend fun getSchoolGroups(
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<SchoolGroup>, NetworkError>

    suspend fun getSchoolGroupsBySchool(
        schoolId: Int,
        page: Int,
        pageSize: Int,
        search: String?
    ): Result<List<SchoolGroup>, NetworkError>

    suspend fun create(
        schoolId: Int,
        groupCode: String
    ): Result<SchoolGroup, NetworkError>

    suspend fun update(
        id: Int,
        groupCode: String,
        isActive: Boolean
    ): Result<SchoolGroup, NetworkError>

    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
