package com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModelRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class UpdateFrameModelUseCase(
    private val repository: FrameModelRepository
) {
    suspend operator fun invoke(id: Int, name: String): Result<FrameModel, NetworkError> {
        return repository.update(id = id, name = name)
    }
}
