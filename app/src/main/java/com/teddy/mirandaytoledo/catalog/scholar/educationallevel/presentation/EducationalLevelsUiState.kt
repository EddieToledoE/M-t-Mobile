package com.teddy.mirandaytoledo.catalog.scholar.educationallevel.presentation

import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface EducationalLevelsUiState {
    data object Loading : EducationalLevelsUiState
    data class Success(val levels: List<EducationalLevel>) : EducationalLevelsUiState
    data class Error(val error: NetworkError) : EducationalLevelsUiState
}
