package com.teddy.mirandaytoledo.register.data.networking

import com.teddy.mirandaytoledo.core.data.networking.constructUrl
import com.teddy.mirandaytoledo.core.data.networking.safeCall
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.data.dto.OrderRegistrationResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RegisterRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun submitRegistration(
        request: CreateOrderRegistrationRequestDto
    ): Result<OrderRegistrationResponseDto, NetworkError> {
        return safeCall {
            httpClient.post(urlString = constructUrl("/api/order-registrations")) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }
}
