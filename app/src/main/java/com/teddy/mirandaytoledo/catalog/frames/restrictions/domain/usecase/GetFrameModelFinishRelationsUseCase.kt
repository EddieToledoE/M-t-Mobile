package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetFrameModelFinishRelationsUseCase(
    private val repository: FrameModelFinishRelationRepository
) {
    suspend operator fun invoke(
        frameModelId: Int? = null,
        finishId: Int? = null
    ): Result<List<FrameModelFinishRelation>, NetworkError> {
        return repository.getAll(frameModelId = frameModelId, finishId = finishId)
    }
}
