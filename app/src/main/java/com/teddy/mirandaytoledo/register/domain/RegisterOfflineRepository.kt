package com.teddy.mirandaytoledo.register.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import kotlinx.coroutines.flow.Flow

interface RegisterOfflineRepository {
    fun observeCatalogBundle(): Flow<RegisterCatalogBundle>
    fun observePendingRegistrations(): Flow<List<PendingRegistration>>

    suspend fun syncCatalogs(): EmptyResult<NetworkError>
    suspend fun savePendingRegistration(
        request: CreateOrderRegistrationRequestDto,
        studentFullName: String,
        schoolLabel: String,
        productTypeName: String
    ): Long

    suspend fun submitPendingRegistration(localId: Long): Result<OrderRegistrationResult, NetworkError>
    suspend fun deletePendingRegistration(localId: Long)
}
