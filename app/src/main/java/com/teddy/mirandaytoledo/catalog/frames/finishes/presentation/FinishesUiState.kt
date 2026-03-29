package com.teddy.mirandaytoledo.catalog.frames.finishes.presentation

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface FinishesUiState {
    data object Loading : FinishesUiState
    data class Success(val finishes: List<Finish>) : FinishesUiState
    data class Error(val error: NetworkError) : FinishesUiState
}
