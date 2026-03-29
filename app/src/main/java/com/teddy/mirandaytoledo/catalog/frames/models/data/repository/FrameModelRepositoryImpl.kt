package com.teddy.mirandaytoledo.catalog.frames.models.data.repository

import com.teddy.mirandaytoledo.catalog.frames.models.data.dto.CreateFrameModelRequest
import com.teddy.mirandaytoledo.catalog.frames.models.data.dto.UpdateFrameModelRequest
import com.teddy.mirandaytoledo.catalog.frames.models.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.models.data.networking.FrameModelRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModelRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class FrameModelRepositoryImpl(
    private val remoteDataSource: FrameModelRemoteDataSource
) : FrameModelRepository {

    override suspend fun getAll(): Result<List<FrameModel>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(name: String): Result<FrameModel, NetworkError> {
        return remoteDataSource.create(
            CreateFrameModelRequest(name = name)
        ).map { it.toDomain() }
    }

    override suspend fun update(id: Int, name: String): Result<FrameModel, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateFrameModelRequest(name = name)
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
