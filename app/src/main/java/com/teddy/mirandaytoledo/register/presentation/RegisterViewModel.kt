package com.teddy.mirandaytoledo.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishColorRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.GetSizesUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.GetPriceRulesUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.GetProductTypesUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationOrderItemRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationOrderRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationPaymentRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationStudentRequestDto
import com.teddy.mirandaytoledo.register.data.dto.CreateRegistrationTutorRequestDto
import com.teddy.mirandaytoledo.register.domain.usecase.SubmitOrderRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val getEducationalLevelsUseCase: GetEducationalLevelsUseCase,
    private val getSchoolsUseCase: GetSchoolsUseCase,
    private val getSchoolGroupsUseCase: GetSchoolGroupsUseCase,
    private val getProductTypesUseCase: GetProductTypesUseCase,
    private val getFinishesUseCase: GetFinishesUseCase,
    private val getSizesUseCase: GetSizesUseCase,
    private val getFrameModelFinishRelationsUseCase: GetFrameModelFinishRelationsUseCase,
    private val getFrameModelFinishColorRelationsUseCase: GetFrameModelFinishColorRelationsUseCase,
    private val getPriceRulesUseCase: GetPriceRulesUseCase,
    private val submitOrderRegistrationUseCase: SubmitOrderRegistrationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCatalogs()
    }

    fun loadCatalogs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val levelsResult = getEducationalLevelsUseCase()) {
                is Result.Error -> return@launch setError(levelsResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(educationalLevels = levelsResult.data)
            }

            when (val schoolsResult = getSchoolsUseCase(page = 1, pageSize = 500)) {
                is Result.Error -> return@launch setError(schoolsResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(schools = schoolsResult.data)
            }

            when (val groupsResult = getSchoolGroupsUseCase(page = 1, pageSize = 500)) {
                is Result.Error -> return@launch setError(groupsResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(schoolGroups = groupsResult.data)
            }

            when (val productTypesResult = getProductTypesUseCase()) {
                is Result.Error -> return@launch setError(productTypesResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(productTypes = productTypesResult.data)
            }

            when (val finishesResult = getFinishesUseCase()) {
                is Result.Error -> return@launch setError(finishesResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(finishes = finishesResult.data)
            }

            when (val sizesResult = getSizesUseCase()) {
                is Result.Error -> return@launch setError(sizesResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(sizes = sizesResult.data)
            }

            when (val relationResult = getFrameModelFinishRelationsUseCase()) {
                is Result.Error -> return@launch setError(relationResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(frameModelFinishRelations = relationResult.data)
            }

            when (val colorRelationResult = getFrameModelFinishColorRelationsUseCase()) {
                is Result.Error -> return@launch setError(colorRelationResult.error)
                is Result.Success -> _uiState.value = _uiState.value.copy(frameModelFinishColorRelations = colorRelationResult.data)
            }

            when (val priceRulesResult = getPriceRulesUseCase()) {
                is Result.Error -> return@launch setError(priceRulesResult.error)
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        priceRules = priceRulesResult.data
                    )
                }
            }
        }
    }

    fun setStep(step: RegisterStep) {
        _uiState.value = _uiState.value.copy(currentStep = step, error = null)
    }

    fun nextStep() {
        val next = when (_uiState.value.currentStep) {
            RegisterStep.PERSONAL -> RegisterStep.PRODUCT
            RegisterStep.PRODUCT -> RegisterStep.PAYMENT
            RegisterStep.PAYMENT -> RegisterStep.PAYMENT
        }
        _uiState.value = _uiState.value.copy(currentStep = next, error = null)
    }

    fun previousStep() {
        val previous = when (_uiState.value.currentStep) {
            RegisterStep.PERSONAL -> RegisterStep.PERSONAL
            RegisterStep.PRODUCT -> RegisterStep.PERSONAL
            RegisterStep.PAYMENT -> RegisterStep.PRODUCT
        }
        _uiState.value = _uiState.value.copy(currentStep = previous, error = null)
    }

    fun updateState(transform: (RegisterUiState) -> RegisterUiState) {
        _uiState.value = transform(_uiState.value).copy(submitSuccess = null, error = null).sanitize()
    }

    fun submitRegistration() {
        val state = _uiState.value
        val schoolGroupId = state.selectedSchoolGroupId ?: return
        val productTypeId = state.selectedProductTypeId ?: return
        val quantity = state.quantity.toIntOrNull() ?: return

        val request = CreateOrderRegistrationRequestDto(
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
                    manualUnitPrice = state.manualUnitPrice.toDoubleOrNull(),
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

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null, submitSuccess = null)
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
            manualUnitPrice = if (selectedProductType?.requiresFinish == false && selectedProductType.requiresSize == false) {
                manualUnitPrice
            } else {
                ""
            }
        )
    }

    private fun setError(error: NetworkError) {
        _uiState.value = _uiState.value.copy(isLoading = false, error = error)
    }
}
