package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository

class SyncRegisterCatalogsUseCase(
    private val repository: RegisterOfflineRepository
) {
    suspend operator fun invoke() = repository.syncCatalogs()
}
