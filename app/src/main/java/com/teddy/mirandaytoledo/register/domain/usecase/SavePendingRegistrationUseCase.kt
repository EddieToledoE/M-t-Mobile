package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository

class SavePendingRegistrationUseCase(
    private val repository: RegisterOfflineRepository
) {
    suspend operator fun invoke(
        request: CreateOrderRegistrationRequestDto,
        studentFullName: String,
        schoolLabel: String,
        productTypeName: String
    ) = repository.savePendingRegistration(
        request = request,
        studentFullName = studentFullName,
        schoolLabel = schoolLabel,
        productTypeName = productTypeName
    )
}
