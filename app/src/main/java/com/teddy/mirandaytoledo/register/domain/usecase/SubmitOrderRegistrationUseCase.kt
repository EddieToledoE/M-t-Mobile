package com.teddy.mirandaytoledo.register.domain.usecase

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.domain.OrderRegistrationResult
import com.teddy.mirandaytoledo.register.domain.RegisterRepository

class SubmitOrderRegistrationUseCase(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(
        request: CreateOrderRegistrationRequestDto
    ): Result<OrderRegistrationResult, NetworkError> {
        return repository.submitRegistration(request)
    }
}
