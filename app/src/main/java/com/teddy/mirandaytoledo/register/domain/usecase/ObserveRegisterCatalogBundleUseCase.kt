package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository

class ObserveRegisterCatalogBundleUseCase(
    private val repository: RegisterOfflineRepository
) {
    operator fun invoke() = repository.observeCatalogBundle()
}
