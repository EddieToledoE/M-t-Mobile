package com.teddy.mirandaytoledo.register.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRegistrationResponseDto(
    val order: RegistrationOrderDto,
    val summary: OrderRegistrationSummaryDto
)

@Serializable
data class RegistrationOrderDto(
    val id: Int,
    val totalAmount: Double
)

@Serializable
data class OrderRegistrationSummaryDto(
    val orderId: Int,
    val total: Double,
    val paid: Double,
    val remaining: Double,
    val itemsCount: Int,
    val paymentsCount: Int,
    val isFullyPaid: Boolean
)
