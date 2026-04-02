package com.teddy.mirandaytoledo.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationOrderItemRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationOrderRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationPaymentRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationStudentRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationTutorRequestDto
import com.teddy.mirandaytoledo.register.domain.RegisterCatalogBundle
import com.teddy.mirandaytoledo.register.domain.usecase.ObserveRegisterCatalogBundleUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SavePendingRegistrationUseCase
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitOrderRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    observeRegisterCatalogBundleUseCase: ObserveRegisterCatalogBundleUseCase,
    private val submitOrderRegistrationUseCase: SubmitOrderRegistrationUseCase,
    private val savePendingRegistrationUseCase: SavePendingRegistrationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeRegisterCatalogBundleUseCase().collect { bundle ->
                _uiState.value = _uiState.value
                    .copy(
                        isLoading = false,
                        educationalLevels = bundle.educationalLevels,
                        schools = bundle.schools,
                        schoolGroups = bundle.schoolGroups,
                        productTypes = bundle.productTypes,
                        finishes = bundle.finishes,
                        sizes = bundle.sizes,
                        frameModelFinishRelations = bundle.frameModelFinishRelations,
                        frameModelFinishColorRelations = bundle.frameModelFinishColorRelations,
                        priceRules = bundle.priceRules
                    )
                    .sanitize()
            }
        }
    }

    fun setStep(step: RegisterStep) {
        _uiState.value = _uiState.value.copy(currentStep = step, error = null, pendingSaveSuccess = false)
    }

    fun nextStep() {
        val next = when (_uiState.value.currentStep) {
            RegisterStep.PERSONAL -> RegisterStep.PRODUCT
            RegisterStep.PRODUCT -> RegisterStep.PAYMENT
            RegisterStep.PAYMENT -> RegisterStep.PAYMENT
        }
        _uiState.value = _uiState.value.copy(currentStep = next, error = null, pendingSaveSuccess = false)
    }

    fun previousStep() {
        val previous = when (_uiState.value.currentStep) {
            RegisterStep.PERSONAL -> RegisterStep.PERSONAL
            RegisterStep.PRODUCT -> RegisterStep.PERSONAL
            RegisterStep.PAYMENT -> RegisterStep.PRODUCT
        }
        _uiState.value = _uiState.value.copy(currentStep = previous, error = null, pendingSaveSuccess = false)
    }

    fun updateState(transform: (RegisterUiState) -> RegisterUiState) {
        _uiState.value = transform(_uiState.value)
            .copy(submitSuccess = null, error = null, pendingSaveSuccess = false)
            .sanitize()
    }

    fun submitRegistration() {
        val request = buildRequest() ?: return
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                error = null,
                submitSuccess = null,
                pendingSaveSuccess = false
            )
            submitOrderRegistrationUseCase(request)
                .onSuccess { result ->
                    _uiState.value = RegisterUiState(
                        isLoading = false,
                        submitSuccess = result,
                        educationalLevels = state.educationalLevels,
                        schools = state.schools,
                        schoolGroups = state.schoolGroups,
                        productTypes = state.productTypes,
                        finishes = state.finishes,
                        sizes = state.sizes,
                        frameModelFinishRelations = state.frameModelFinishRelations,
                        frameModelFinishColorRelations = state.frameModelFinishColorRelations,
                        priceRules = state.priceRules
                    )
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = error)
                }
        }
    }

    fun savePendingRegistration() {
        val request = buildRequest() ?: return
        val state = _uiState.value

        viewModelScope.launch {
            savePendingRegistrationUseCase(
                request = request,
                studentFullName = buildStudentFullName(state),
                schoolLabel = buildSchoolLabel(state),
                productTypeName = state.productTypes.firstOrNull { it.id == state.selectedProductTypeId }?.name.orEmpty()
            )
            _uiState.value = RegisterUiState(
                isLoading = false,
                pendingSaveSuccess = true,
                educationalLevels = state.educationalLevels,
                schools = state.schools,
                schoolGroups = state.schoolGroups,
                productTypes = state.productTypes,
                finishes = state.finishes,
                sizes = state.sizes,
                frameModelFinishRelations = state.frameModelFinishRelations,
                frameModelFinishColorRelations = state.frameModelFinishColorRelations,
                priceRules = state.priceRules
            )
        }
    }

    private fun buildRequest(): CreateOrderRegistrationRequestDto? {
        val state = _uiState.value
        val schoolGroupId = state.selectedSchoolGroupId ?: return null
        val productTypeId = state.selectedProductTypeId ?: return null
        val quantity = state.quantity.toIntOrNull() ?: return null

        return CreateOrderRegistrationRequestDto(
            tutor = CreateRegistrationTutorRequestDto(
                firstName = state.tutorFirstName.trim(),
                lastName1 = state.tutorLastName1.trim(),
                lastName2 = state.tutorLastName2.trim().ifBlank { null },
                phone = state.tutorPhone.trim().ifBlank { null }
            ),
            student = CreateRegistrationStudentRequestDto(
                schoolGroupId = schoolGroupId,
                firstName = state.studentFirstName.trim(),
                lastName1 = state.studentLastName1.trim(),
                lastName2 = state.studentLastName2.trim().ifBlank { null },
                photoReference = state.photoReference.trim().ifBlank { null },
                wantsFacialCleanup = state.wantsFacialCleanup
            ),
            order = CreateRegistrationOrderRequestDto(
                notes = state.orderNotes.trim().ifBlank { null }
            ),
            items = listOf(
                CreateRegistrationOrderItemRequestDto(
                    productTypeId = productTypeId,
                    quantity = quantity,
                    finishId = state.selectedFinishId,
                    sizeId = state.selectedSizeId,
                    frameModelId = state.selectedFrameModelId,
                    colorId = state.selectedColorId,
                    manualUnitPrice = state.manualUnitPrice.toDoubleOrNull()
                        ?.takeIf { state.useManualUnitPrice && it > 0.0 },
                    notes = state.itemNotes.trim().ifBlank { null }
                )
            ),
            initialPayment = state.downPayment.toDoubleOrNull()?.takeIf { it > 0 }?.let { amount ->
                CreateRegistrationPaymentRequestDto(
                    amount = amount,
                    method = state.paymentMethod,
                    note = state.paymentNote.trim().ifBlank { null }
                )
            }
        )
    }

    private fun buildStudentFullName(state: RegisterUiState): String {
        return listOf(state.studentFirstName, state.studentLastName1, state.studentLastName2)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun buildSchoolLabel(state: RegisterUiState): String {
        val selectedLevel = state.educationalLevels.firstOrNull { it.id == state.selectedEducationalLevelId }
        val selectedSchool = state.schools.firstOrNull { it.id == state.selectedSchoolId }
        val selectedGroup = state.schoolGroups.firstOrNull { it.id == state.selectedSchoolGroupId }
        return listOfNotNull(selectedLevel?.name, selectedSchool?.name, selectedGroup?.groupCode)
            .joinToString(" • ")
    }

    private fun RegisterUiState.sanitize(): RegisterUiState {
        val selectedLevel = educationalLevels.firstOrNull { it.id == selectedEducationalLevelId }
        val availableSchools = schools.filter {
            selectedLevel == null || it.educationalLevelId == selectedLevel.id
        }
        val safeSchoolId = selectedSchoolId?.takeIf { id -> availableSchools.any { it.id == id } }

        val availableGroups = schoolGroups.filter {
            safeSchoolId == null || it.schoolId == safeSchoolId
        }
        val safeGroupId = selectedSchoolGroupId?.takeIf { id -> availableGroups.any { it.id == id } }

        val selectedProductType = productTypes.firstOrNull { it.id == selectedProductTypeId }
        val safeFinishId = if (selectedProductType?.requiresFinish == true) selectedFinishId else null
        val safeSizeId = if (selectedProductType?.requiresSize == true) {
            selectedSizeId?.takeIf { id ->
                sizes.filter { size ->
                    size.isActive && selectedProductType.allowedSizeGroup.equals(size.sizeGroup, ignoreCase = true)
                }.any { it.id == id }
            }
        } else {
            null
        }

        val availableModels = if (safeFinishId != null) {
            frameModelFinishRelations.filter { it.finishId == safeFinishId }
        } else {
            emptyList()
        }
        val safeFrameModelId = if (selectedProductType?.requiresFrameModel == true) {
            selectedFrameModelId?.takeIf { id -> availableModels.any { it.frameModelId == id } }
        } else {
            null
        }

        val availableColors = if (safeFinishId != null && safeFrameModelId != null) {
            frameModelFinishColorRelations.filter {
                it.finishId == safeFinishId && it.frameModelId == safeFrameModelId
            }
        } else {
            emptyList()
        }
        val safeColorId = if (selectedProductType?.requiresColor == true) {
            selectedColorId?.takeIf { id -> availableColors.any { it.colorId == id } }
        } else {
            null
        }

        return copy(
            selectedSchoolId = safeSchoolId,
            selectedSchoolGroupId = safeGroupId,
            selectedFinishId = safeFinishId,
            selectedSizeId = safeSizeId,
            selectedFrameModelId = safeFrameModelId,
            selectedColorId = safeColorId,
            useManualUnitPrice = useManualUnitPrice && selectedProductType != null,
            manualUnitPrice = if (useManualUnitPrice && selectedProductType != null) manualUnitPrice else ""
        )
    }

    private fun setError(error: NetworkError) {
        _uiState.value = _uiState.value.copy(isLoading = false, error = error)
    }
}
