package com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelationRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteFrameModelFinishRelationUseCase(
    private val repository: FrameModelFinishRelationRepository
) {
    suspend operator fun invoke(frameModelId: Int, finishId: Int): EmptyResult<NetworkError> {
        return repository.delete(frameModelId = frameModelId, finishId = finishId)
    }
}
