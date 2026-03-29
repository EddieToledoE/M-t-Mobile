package com.teddy.mirandaytoledo.catalog.frames.sizes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.CreateSizeUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.DeleteSizeUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.GetSizesUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.UpdateSizeUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SizesViewModel(
    private val getSizesUseCase: GetSizesUseCase,
    private val createSizeUseCase: CreateSizeUseCase,
    private val updateSizeUseCase: UpdateSizeUseCase,
    private val deleteSizeUseCase: DeleteSizeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SizesUiState>(SizesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadSizes()
    }

    fun loadSizes() {
        viewModelScope.launch {
            _uiState.value = SizesUiState.Loading
            getSizesUseCase()
                .onSuccess { sizes ->
                    _uiState.value = SizesUiState.Success(sizes)
                }
                .onError { error ->
                    _uiState.value = SizesUiState.Error(error)
                }
        }
    }

    fun createSize(name: String, sizeGroup: String) {
        viewModelScope.launch {
            createSizeUseCase(
                name = name.trim(),
                sizeGroup = sizeGroup.trim().lowercase()
            )
                .onSuccess { loadSizes() }
                .onError { error ->
                    _uiState.value = SizesUiState.Error(error)
                }
        }
    }

    fun updateSize(id: Int, name: String, sizeGroup: String, isActive: Boolean) {
        viewModelScope.launch {
            updateSizeUseCase(
                id = id,
                name = name.trim(),
                sizeGroup = sizeGroup.trim().lowercase(),
                isActive = isActive
            )
                .onSuccess { loadSizes() }
                .onError { error ->
                    _uiState.value = SizesUiState.Error(error)
                }
        }
    }

    fun deleteSize(id: Int) {
        viewModelScope.launch {
            deleteSizeUseCase(id = id)
                .onSuccess { loadSizes() }
                .onError { error ->
                    _uiState.value = SizesUiState.Error(error)
                }
        }
    }
}
