package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetFrameModelFinishColorRelationsUseCase(
    private val repository: FrameModelFinishColorRelationRepository
) {
    suspend operator fun invoke(
        frameModelId: Int? = null,
        finishId: Int? = null
    ): Result<List<FrameModelFinishColorRelation>, NetworkError> {
        return repository.getAll(frameModelId = frameModelId, finishId = finishId)
    }
}
