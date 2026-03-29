package com.teddy.mirandaytoledo.catalog.frames.models.presentation

import com.teddy.mirandaytoledo.catalog.frames.models.domain.FrameModel
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface FrameModelsUiState {
    data object Loading : FrameModelsUiState
    data class Success(val models: List<FrameModel>) : FrameModelsUiState
    data class Error(val error: NetworkError) : FrameModelsUiState
}
