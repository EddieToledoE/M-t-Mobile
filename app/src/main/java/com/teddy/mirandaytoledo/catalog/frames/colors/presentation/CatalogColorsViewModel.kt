package com.teddy.mirandaytoledo.catalog.frames.colors.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.CreateCatalogColorUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.DeleteCatalogColorUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.GetCatalogColorsUseCase
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.usecase.UpdateCatalogColorUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogColorsViewModel(
    private val getCatalogColorsUseCase: GetCatalogColorsUseCase,
    private val createCatalogColorUseCase: CreateCatalogColorUseCase,
    private val updateCatalogColorUseCase: UpdateCatalogColorUseCase,
    private val deleteCatalogColorUseCase: DeleteCatalogColorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatalogColorsUiState>(CatalogColorsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadColors()
    }

    fun loadColors() {
        viewModelScope.launch {
            _uiState.value = CatalogColorsUiState.Loading
            getCatalogColorsUseCase()
                .onSuccess { colors ->
                    _uiState.value = CatalogColorsUiState.Success(colors)
                }
                .onError { error ->
                    _uiState.value = CatalogColorsUiState.Error(error)
                }
        }
    }

    fun createColor(name: String, hex: String?) {
        viewModelScope.launch {
            createCatalogColorUseCase(
                name = name.trim(),
                hex = hex?.trim()
            )
                .onSuccess { loadColors() }
                .onError { error ->
                    _uiState.value = CatalogColorsUiState.Error(error)
                }
        }
    }

    fun updateColor(id: Int, name: String, hex: String?) {
        viewModelScope.launch {
            updateCatalogColorUseCase(
                id = id,
                name = name.trim(),
                hex = hex?.trim()
            )
                .onSuccess { loadColors() }
                .onError { error ->
                    _uiState.value = CatalogColorsUiState.Error(error)
                }
        }
    }

    fun deleteColor(id: Int) {
        viewModelScope.launch {
            deleteCatalogColorUseCase(id = id)
                .onSuccess { loadColors() }
                .onError { error ->
                    _uiState.value = CatalogColorsUiState.Error(error)
                }
        }
    }
}
