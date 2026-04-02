package com.teddy.mirandaytoledo.home.presentation

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.register.domain.PendingRegistration

sealed interface HomeEvent {
    data object SyncSuccess : HomeEvent
    data object PendingDeleted : HomeEvent
    data class PendingSent(val orderId: Int) : HomeEvent
    data class Error(val error: NetworkError) : HomeEvent
}

data class HomeUiState(
    val isSyncingCatalogs: Boolean = false,
    val sendingPendingId: Long? = null,
    val pendingRegistrations: List<PendingRegistration> = emptyList(),
    val event: HomeEvent? = null
)
