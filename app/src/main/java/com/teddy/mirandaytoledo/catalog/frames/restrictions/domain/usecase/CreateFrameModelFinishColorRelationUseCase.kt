package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateFrameModelFinishColorRelationUseCase(
    private val repository: FrameModelFinishColorRelationRepository
) {
    suspend operator fun invoke(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): Result<FrameModelFinishColorRelation, NetworkError> {
        return repository.create(
            frameModelId = frameModelId,
            finishId = finishId,
            colorId = colorId
        )
    }
}
