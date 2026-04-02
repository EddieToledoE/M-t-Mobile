package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository

class DeletePendingRegistrationUseCase(
    private val repository: RegisterOfflineRepository
) {
    suspend operator fun invoke(localId: Long) = repository.deletePendingRegistration(localId)
}
