package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateFrameModelFinishRelationUseCase(
    private val repository: FrameModelFinishRelationRepository
) {
    suspend operator fun invoke(
        frameModelId: Int,
        finishId: Int
    ): Result<FrameModelFinishRelation, NetworkError> {
        return repository.create(frameModelId = frameModelId, finishId = finishId)
    }
}
