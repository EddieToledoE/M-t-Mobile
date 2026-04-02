package com.teddy.mirandaytoledo.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import com.teddy.mirandaytoledo.register.domain.usecase.DeletePendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.ObservePendingRegistrationsUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitPendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SyncRegisterCatalogsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    observePendingRegistrationsUseCase: ObservePendingRegistrationsUseCase,
    private val syncRegisterCatalogsUseCase: SyncRegisterCatalogsUseCase,
    private val submitPendingRegistrationUseCase: SubmitPendingRegistrationUseCase,
    private val deletePendingRegistrationUseCase: DeletePendingRegistrationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observePendingRegistrationsUseCase().collect { pending ->
                _uiState.value = _uiState.value.copy(pendingRegistrations = pending)
            }
        }
    }

    fun syncCatalogs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncingCatalogs = true, event = null)
            syncRegisterCatalogsUseCase()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSyncingCatalogs = false,
                        event = HomeEvent.SyncSuccess
                    )
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(
                        isSyncingCatalogs = false,
                        event = HomeEvent.Error(error)
                    )
                }
        }
    }

    fun sendPending(localId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(sendingPendingId = localId, event = null)
            submitPendingRegistrationUseCase(localId)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(
                        sendingPendingId = null,
                        event = HomeEvent.PendingSent(result.orderId)
                    )
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(
                        sendingPendingId = null,
                        event = HomeEvent.Error(error)
                    )
                }
        }
    }

    fun deletePending(localId: Long) {
        viewModelScope.launch {
            deletePendingRegistrationUseCase(localId)
            _uiState.value = _uiState.value.copy(event = HomeEvent.PendingDeleted)
        }
    }

    fun consumeEvent() {
        _uiState.value = _uiState.value.copy(event = null)
    }
}
