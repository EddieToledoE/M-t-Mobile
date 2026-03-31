package com.teddy.mirandaytoledo.register.domain

data class OrderRegistrationResult(
    val orderId: Int,
    val total: Double,
    val paid: Double,
    val remaining: Double,
    val itemsCount: Int,
    val paymentsCount: Int,
    val isFullyPaid: Boolean
)
