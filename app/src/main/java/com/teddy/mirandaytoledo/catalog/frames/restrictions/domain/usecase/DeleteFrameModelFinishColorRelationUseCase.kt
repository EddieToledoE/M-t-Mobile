package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishColorRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteFrameModelFinishColorRelationUseCase(
    private val repository: FrameModelFinishColorRelationRepository
) {
    suspend operator fun invoke(
        frameModelId: Int,
        finishId: Int,
        colorId: Int
    ): EmptyResult<NetworkError> {
        return repository.delete(frameModelId = frameModelId, finishId = finishId, colorId = colorId)
    }
}
