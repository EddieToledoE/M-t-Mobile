package com.teddy.mirandaytoledo.register.data.mappers

import com.teddy.mirandaytoledo.register.data.dto.OrderRegistrationResponseDto
import com.teddy.mirandaytoledo.register.domain.OrderRegistrationResult

fun OrderRegistrationResponseDto.toDomain(): OrderRegistrationResult {
    return OrderRegistrationResult(
        orderId = order.id,
        total = summary.total,
        paid = summary.paid,
        remaining = summary.remaining,
        itemsCount = summary.itemsCount,
        paymentsCount = summary.paymentsCount,
        isFullyPaid = summary.isFullyPaid
    )
}
