package com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel
import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModelRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetFrameModelsUseCase(
    private val repository: FrameModelRepository
) {
    suspend operator fun invoke(): Result<List<FrameModel>, NetworkError> {
        return repository.getAll()
    }
}
