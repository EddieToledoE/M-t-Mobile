package com.teddy.mirandaytoledo.catalog.frames.restrictions.data.repository

import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.dto.CreateFrameModelFinishRelationRequest
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.restrictions.data.networking.FrameModelFinishRelationRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class FrameModelFinishRelationRepositoryImpl(
    private val remoteDataSource: FrameModelFinishRelationRemoteDataSource
) : FrameModelFinishRelationRepository {

    override suspend fun getAll(
        frameModelId: Int?,
        finishId: Int?
    ): Result<List<FrameModelFinishRelation>, NetworkError> {
        return remoteDataSource.getAll(frameModelId = frameModelId, finishId = finishId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun create(
        frameModelId: Int,
        finishId: Int
    ): Result<FrameModelFinishRelation, NetworkError> {
        return remoteDataSource.create(
            CreateFrameModelFinishRelationRequest(
                frameModelId = frameModelId,
                finishId = finishId
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(frameModelId: Int, finishId: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(frameModelId = frameModelId, finishId = finishId)
    }
}
