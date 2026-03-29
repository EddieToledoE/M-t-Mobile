package com.teddy.mirandaytoledo.catalog.frames.sizes.data.repository

import com.teddy.mirandaytoledo.catalog.frames.sizes.data.dto.CreateSizeRequest
import com.teddy.mirandaytoledo.catalog.frames.sizes.data.dto.UpdateSizeRequest
import com.teddy.mirandaytoledo.catalog.frames.sizes.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.sizes.data.networking.SizeRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.SizeRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class SizeRepositoryImpl(
    private val remoteDataSource: SizeRemoteDataSource
) : SizeRepository {

    override suspend fun getAll(): Result<List<Size>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(name: String, sizeGroup: String): Result<Size, NetworkError> {
        return remoteDataSource.create(
            CreateSizeRequest(
                name = name,
                sizeGroup = sizeGroup
            )
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        name: String,
        sizeGroup: String,
        isActive: Boolean
    ): Result<Size, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateSizeRequest(
                name = name,
                sizeGroup = sizeGroup,
                isActive = isActive
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
