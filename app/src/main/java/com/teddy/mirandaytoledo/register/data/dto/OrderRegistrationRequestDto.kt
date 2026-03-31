package com.teddy.mirandaytoledo.register.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRegistrationRequestDto(
    val tutor: CreateRegistrationTutorRequestDto,
    val student: CreateRegistrationStudentRequestDto,
    val order: CreateRegistrationOrderRequestDto,
    val items: List<CreateRegistrationOrderItemRequestDto>,
    val initialPayment: CreateRegistrationPaymentRequestDto?
)

@Serializable
data class CreateRegistrationTutorRequestDto(
    val firstName: String,
    val lastName1: String,
    val lastName2: String?,
    val phone: String?
)

@Serializable
data class CreateRegistrationStudentRequestDto(
    val schoolGroupId: Int,
    val firstName: String,
    val lastName1: String,
    val lastName2: String?,
    val photoReference: String?,
    val wantsFacialCleanup: Boolean
)

@Serializable
data class CreateRegistrationOrderRequestDto(
    val notes: String?
)

@Serializable
data class CreateRegistrationOrderItemRequestDto(
    val productTypeId: Int,
    val quantity: Int,
    val finishId: Int?,
    val sizeId: Int?,
    val frameModelId: Int?,
    val colorId: Int?,
    val manualUnitPrice: Double?,
    val notes: String?
)

@Serializable
data class CreateRegistrationPaymentRequestDto(
    val amount: Double,
    val method: String?,
    val note: String?
)
