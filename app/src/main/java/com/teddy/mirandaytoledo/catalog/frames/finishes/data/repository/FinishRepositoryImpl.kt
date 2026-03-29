package com.teddy.mirandaytoledo.catalog.frames.finishes.data.repository

import com.teddy.mirandaytoledo.catalog.frames.finishes.data.dto.CreateFinishRequest
import com.teddy.mirandaytoledo.catalog.frames.finishes.data.dto.UpdateFinishRequest
import com.teddy.mirandaytoledo.catalog.frames.finishes.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.finishes.data.networking.FinishRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class FinishRepositoryImpl(
    private val remoteDataSource: FinishRemoteDataSource
) : FinishRepository {

    override suspend fun getAll(): Result<List<Finish>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(name: String): Result<Finish, NetworkError> {
        return remoteDataSource.create(
            CreateFinishRequest(name = name)
        ).map { it.toDomain() }
    }

    override suspend fun update(id: Int, name: String, isActive: Boolean): Result<Finish, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateFinishRequest(
                name = name,
                isActive = isActive
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
