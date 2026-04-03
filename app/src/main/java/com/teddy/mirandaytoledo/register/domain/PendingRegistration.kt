package com.teddy.mirandaytoledo.register.domain

enum class PendingRegistrationStatus {
    PendingSync,
    Syncing,
    Failed,
    Synced
}

data class PendingRegistration(
    val localId: Long,
    val payloadJson: String,
    val studentFullName: String,
    val schoolLabel: String,
    val productTypeName: String,
    val createdAt: Long,
    val status: PendingRegistrationStatus,
    val lastErrorMessage: String?,
    val lastAttemptAt: Long?,
    val syncedOrderId: Int?
)
