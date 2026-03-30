package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface FrameModelFinishColorRelationRepository {
    suspend fun getAll(
        frameModelId: Int? = null,
        finishId: Int? = null
    ): Result<List<FrameModelFinishColorRelation>, NetworkError>

    suspend fun create(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): Result<FrameModelFinishColorRelation, NetworkError>

    suspend fun delete(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): EmptyResult<NetworkError>
}
