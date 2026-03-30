package com.teddy.mirandaytoledo.catalog.frames.restrictions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.GetCatalogColorsUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.GetFrameModelsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.FrameModelFinishRelation
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.CreateFrameModelFinishColorRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.CreateFrameModelFinishRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.DeleteFrameModelFinishColorRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.DeleteFrameModelFinishRelationUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishColorRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishRelationsUseCase
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FrameRestrictionsViewModel(
    private val getFrameModelsUseCase: GetFrameModelsUseCase,
    private val getFinishesUseCase: GetFinishesUseCase,
    private val getCatalogColorsUseCase: GetCatalogColorsUseCase,
    private val getFrameModelFinishRelationsUseCase: GetFrameModelFinishRelationsUseCase,
    private val createFrameModelFinishRelationUseCase: CreateFrameModelFinishRelationUseCase,
    private val deleteFrameModelFinishRelationUseCase: DeleteFrameModelFinishRelationUseCase,
    private val getFrameModelFinishColorRelationsUseCase: GetFrameModelFinishColorRelationsUseCase,
    private val createFrameModelFinishColorRelationUseCase: CreateFrameModelFinishColorRelationUseCase,
    private val deleteFrameModelFinishColorRelationUseCase: DeleteFrameModelFinishColorRelationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FrameRestrictionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val modelsResult = getFrameModelsUseCase()) {
                is Result.Error -> {
                    setGlobalError(modelsResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(frameModels = modelsResult.data)
                }
            }

            when (val finishesResult = getFinishesUseCase()) {
                is Result.Error -> {
                    setGlobalError(finishesResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(finishes = finishesResult.data)
                }
            }

            when (val colorsResult = getCatalogColorsUseCase()) {
                is Result.Error -> {
                    setGlobalError(colorsResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(colors = colorsResult.data)
                }
            }

            when (val combinationsResult = getFrameModelFinishRelationsUseCase()) {
                is Result.Error -> {
                    setGlobalError(combinationsResult.error)
                    return@launch
                }
                is Result.Success -> {
                    val selected = resolveSelection(
                        currentSelection = _uiState.value.selectedCombination,
                        combinations = combinationsResult.data
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        combinations = combinationsResult.data,
                        selectedCombination = selected
                    )
                    loadColorsForSelection(selected)
                }
            }
        }
    }

    fun selectCombination(combination: FrameModelFinishRelation) {
        _uiState.value = _uiState.value.copy(selectedCombination = combination)
        loadColorsForSelection(combination)
    }

    fun createCombination(frameModelId: Int, finishId: Int) {
        viewModelScope.launch {
            createFrameModelFinishRelationUseCase(
                frameModelId = frameModelId,
                finishId = finishId
            )
                .onSuccess { created ->
                    _uiState.value = _uiState.value.copy(selectedCombination = created, error = null)
                    loadAll()
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(error = error)
                }
        }
    }

    fun deleteCombination(frameModelId: Int, finishId: Int) {
        viewModelScope.launch {
            deleteFrameModelFinishRelationUseCase(frameModelId = frameModelId, finishId = finishId)
                .onSuccess {
                    val currentSelection = _uiState.value.selectedCombination
                    val shouldClearSelection = currentSelection?.frameModelId == frameModelId &&
                        currentSelection.finishId == finishId

                    if (shouldClearSelection) {
                        _uiState.value = _uiState.value.copy(
                            selectedCombination = null,
                            allowedColors = emptyList(),
                            error = null
                        )
                    }
                    loadAll()
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(error = error)
                }
        }
    }

    fun createColorRestriction(colorId: Int) {
        val selected = _uiState.value.selectedCombination ?: return

        viewModelScope.launch {
            createFrameModelFinishColorRelationUseCase(
                frameModelId = selected.frameModelId,
                finishId = selected.finishId,
                colorId = colorId
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(error = null)
                    loadColorsForSelection(selected)
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(error = error)
                }
        }
    }

    fun deleteColorRestriction(colorId: Int) {
        val selected = _uiState.value.selectedCombination ?: return

        viewModelScope.launch {
            deleteFrameModelFinishColorRelationUseCase(
                frameModelId = selected.frameModelId,
                finishId = selected.finishId,
                colorId = colorId
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(error = null)
                    loadColorsForSelection(selected)
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(error = error)
                }
        }
    }

    private fun loadColorsForSelection(selection: FrameModelFinishRelation?) {
        if (selection == null) {
            _uiState.value = _uiState.value.copy(
                isColorSectionLoading = false,
                allowedColors = emptyList()
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isColorSectionLoading = true, error = null)
            getFrameModelFinishColorRelationsUseCase(
                frameModelId = selection.frameModelId,
                finishId = selection.finishId
            )
                .onSuccess { colorRelations ->
                    _uiState.value = _uiState.value.copy(
                        isColorSectionLoading = false,
                        allowedColors = colorRelations
                    )
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(
                        isColorSectionLoading = false,
                        error = error,
                        allowedColors = emptyList()
                    )
                }
        }
    }

    private fun setGlobalError(error: NetworkError) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isColorSectionLoading = false,
            error = error
        )
    }

    private fun resolveSelection(
        currentSelection: FrameModelFinishRelation?,
        combinations: List<FrameModelFinishRelation>
    ): FrameModelFinishRelation? {
        if (combinations.isEmpty()) return null
        return combinations.firstOrNull {
            it.frameModelId == currentSelection?.frameModelId &&
                it.finishId == currentSelection?.finishId
        } ?: combinations.first()
    }
}
