package com.teddy.mirandaytoledo.catalog.frames.colors.presentation

import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface CatalogColorsUiState {
    data object Loading : CatalogColorsUiState
    data class Success(val colors: List<CatalogColor>) : CatalogColorsUiState
    data class Error(val error: NetworkError) : CatalogColorsUiState
}
