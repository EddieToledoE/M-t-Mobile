package com.teddy.mirandaytoledo.register.domain

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto

interface RegisterRepository {
    suspend fun submitRegistration(
        request: CreateOrderRegistrationRequestDto
    ): Result<OrderRegistrationResult, NetworkError>
}
