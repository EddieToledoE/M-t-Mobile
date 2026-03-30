package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface FrameModelFinishRelationRepository {
    suspend fun getAll(
        frameModelId: Int? = null,
        finishId: Int? = null
    ): Result<List<FrameModelFinishRelation>, NetworkError>

    suspend fun create(frameModelId: Int, finishId: Int): Result<FrameModelFinishRelation, NetworkError>

    suspend fun delete(frameModelId: Int, finishId: Int): EmptyResult<NetworkError>
}
