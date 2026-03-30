package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.repository

import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto.CreateFrameModelFinishColorRelationRequest
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.networking.FrameModelFinishColorRelationRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class FrameModelFinishColorRelationRepositoryImpl(
    private val remoteDataSource: FrameModelFinishColorRelationRemoteDataSource
) : FrameModelFinishColorRelationRepository {

    override suspend fun getAll(
        frameModelId: Int?,
        finishId: Int?
    ): Result<List<FrameModelFinishColorRelation>, NetworkError> {
        return remoteDataSource.getAll(frameModelId = frameModelId, finishId = finishId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun create(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): Result<FrameModelFinishColorRelation, NetworkError> {
        return remoteDataSource.create(
            CreateFrameModelFinishColorRelationRequest(
                frameModelId = frameModelId,
                finishId = finishId,
                colorId = colorId
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): EmptyResult<NetworkError> {
        return remoteDataSource.delete(
            frameModelId = frameModelId,
            finishId = finishId,
            colorId = colorId
        )
    }
}
