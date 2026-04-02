package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository

class ObservePendingRegistrationsUseCase(
    private val repository: RegisterOfflineRepository
) {
    operator fun invoke() = repository.observePendingRegistrations()
}
