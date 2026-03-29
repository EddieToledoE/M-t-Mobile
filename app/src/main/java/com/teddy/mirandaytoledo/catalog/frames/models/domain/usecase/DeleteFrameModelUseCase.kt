package com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModelRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteFrameModelUseCase(
    private val repository: FrameModelRepository
) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
