package com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetFinishesUseCase(
    private val repository: FinishRepository
) {
    suspend operator fun invoke(): Result<List<Finish>, NetworkError> {
        return repository.getAll()
    }
}
