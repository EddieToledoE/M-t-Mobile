package com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateFinishUseCase(
    private val repository: FinishRepository
) {
    suspend operator fun invoke(name: String): Result<Finish, NetworkError> {
        return repository.create(name = name)
    }
}
