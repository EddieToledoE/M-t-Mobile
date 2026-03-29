package com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteFinishUseCase(
    private val repository: FinishRepository
) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
