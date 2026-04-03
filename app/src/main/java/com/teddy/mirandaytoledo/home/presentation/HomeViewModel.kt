package com.teddy.mirandaytoledo.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.domain.OrderShareInfoBuilder
import com.teddy.mirandaytoledo.register.domain.RegisterCatalogBundle
import com.teddy.mirandaytoledo.register.domain.usecase.DeletePendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.ObservePendingRegistrationsUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.ObserveRegisterCatalogBundleUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitPendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SyncRegisterCatalogsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeViewModel(
    observePendingRegistrationsUseCase: ObservePendingRegistrationsUseCase,
    observeRegisterCatalogBundleUseCase: ObserveRegisterCatalogBundleUseCase,
    private val syncRegisterCatalogsUseCase: SyncRegisterCatalogsUseCase,
    private val submitPendingRegistrationUseCase: SubmitPendingRegistrationUseCase,
    private val deletePendingRegistrationUseCase: DeletePendingRegistrationUseCase,
    private val json: Json
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private var latestCatalogBundle: RegisterCatalogBundle = RegisterCatalogBundle()

    init {
        viewModelScope.launch {
            observePendingRegistrationsUseCase().collect { pending ->
                _uiState.value = _uiState.value.copy(pendingRegistrations = pending)
            }
        }
        viewModelScope.launch {
            observeRegisterCatalogBundleUseCase().collect { bundle ->
                latestCatalogBundle = bundle
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
            val pending = _uiState.value.pendingRegistrations.firstOrNull { it.localId == localId }
            submitPendingRegistrationUseCase(localId)
                .onSuccess { result ->
                    val request = pending?.payloadJson?.let {
                        json.decodeFromString(CreateOrderRegistrationRequestDto.serializer(), it)
                    }
                    val shareInfo = request?.let {
                        buildShareInfo(
                            request = it,
                            orderId = result.orderId,
                            total = result.total,
                            paid = result.paid,
                            remaining = result.remaining
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        sendingPendingId = null,
                        event = shareInfo?.let { HomeEvent.PendingSent(it) } ?: HomeEvent.SyncSuccess
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

    private fun buildShareInfo(
        request: CreateOrderRegistrationRequestDto,
        orderId: Int,
        total: Double,
        paid: Double,
        remaining: Double
    ) = OrderShareInfoBuilder.build(
        orderId = orderId,
        phone = request.tutor.phone,
        studentName = listOf(
            request.student.firstName,
            request.student.lastName1,
            request.student.lastName2
        ).filterNotNull().filter { it.isNotBlank() }.joinToString(" "),
        schoolLabel = buildSchoolLabel(request.student.schoolGroupId),
        productTypeName = latestCatalogBundle.productTypes.firstOrNull {
            it.id == request.items.firstOrNull()?.productTypeId
        }?.name.orEmpty(),
        options = buildOptions(request),
        photoReference = request.student.photoReference,
        total = total,
        paid = paid,
        remaining = remaining,
        notes = listOfNotNull(request.items.firstOrNull()?.notes, request.order.notes)
            .filter { it.isNotBlank() }
            .joinToString(" • ")
            .ifBlank { null }
    )

    private fun buildSchoolLabel(groupId: Int): String {
        val group = latestCatalogBundle.schoolGroups.firstOrNull { it.id == groupId }
        val school = latestCatalogBundle.schools.firstOrNull { it.id == group?.schoolId }
        val level = latestCatalogBundle.educationalLevels.firstOrNull { it.id == school?.educationalLevelId }
        return listOfNotNull(level?.name, school?.name, group?.groupCode).joinToString(" • ")
    }

    private fun buildOptions(request: CreateOrderRegistrationRequestDto): List<String> {
        val item = request.items.firstOrNull() ?: return emptyList()
        val finish = latestCatalogBundle.finishes.firstOrNull { it.id == item.finishId }?.name
        val size = latestCatalogBundle.sizes.firstOrNull { it.id == item.sizeId }?.name
        val model = latestCatalogBundle.frameModelFinishRelations.firstOrNull {
            it.frameModelId == item.frameModelId && it.finishId == item.finishId
        }?.frameModelName
        val color = latestCatalogBundle.frameModelFinishColorRelations.firstOrNull {
            it.colorId == item.colorId &&
                it.frameModelId == item.frameModelId &&
                it.finishId == item.finishId
        }?.colorName
        return listOfNotNull(finish, size, model, color)
    }
}
