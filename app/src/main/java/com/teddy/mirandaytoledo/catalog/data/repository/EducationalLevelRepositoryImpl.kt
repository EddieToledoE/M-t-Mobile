package com.teddy.mirandaytoledo.catalog.data.repository

import com.teddy.mirandaytoledo.catalog.data.dto.CreateEducationalLevelRequest
import com.teddy.mirandaytoledo.catalog.data.dto.UpdateEducationalLevelRequest
import com.teddy.mirandaytoledo.catalog.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.data.networking.EducationalLevelRemoteDataSource
import com.teddy.mirandaytoledo.catalog.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.domain.EducationalLevelRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class EducationalLevelRepositoryImpl(
    private val remoteDataSource: EducationalLevelRemoteDataSource
) : EducationalLevelRepository {

    override suspend fun getAll(): Result<List<EducationalLevel>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(name: String, maxGrade: Int): Result<EducationalLevel, NetworkError> {
        return remoteDataSource.create(
            CreateEducationalLevelRequest(name = name, maxGrade = maxGrade)
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        name: String,
        maxGrade: Int,
        isActive: Boolean
    ): Result<EducationalLevel, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateEducationalLevelRequest(name = name, maxGrade = maxGrade, isActive = isActive)
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
