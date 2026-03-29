package com.teddy.mirandaytoledo.catalog.frames.sizes.presentation

import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface SizesUiState {
    data object Loading : SizesUiState
    data class Success(val sizes: List<Size>) : SizesUiState
    data class Error(val error: NetworkError) : SizesUiState
}
