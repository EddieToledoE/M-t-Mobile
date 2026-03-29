package com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.FinishRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class UpdateFinishUseCase(
    private val repository: FinishRepository
) {
    suspend operator fun invoke(id: Int, name: String, isActive: Boolean): Result<Finish, NetworkError> {
        return repository.update(id = id, name = name, isActive = isActive)
    }
}
