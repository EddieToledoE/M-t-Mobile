package com.teddy.mirandaytoledo.register.data.repository

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.data.mappers.toDomain
import com.teddy.mirandaytoledo.register.data.networking.RegisterRemoteDataSource
import com.teddy.mirandaytoledo.register.domain.OrderRegistrationResult
import com.teddy.mirandaytoledo.register.domain.RegisterRepository

class RegisterRepositoryImpl(
    private val remoteDataSource: RegisterRemoteDataSource
) : RegisterRepository {
    override suspend fun submitRegistration(
        request: CreateOrderRegistrationRequestDto
    ): Result<OrderRegistrationResult, NetworkError> {
        return remoteDataSource.submitRegistration(request).map { it.toDomain() }
    }
}
