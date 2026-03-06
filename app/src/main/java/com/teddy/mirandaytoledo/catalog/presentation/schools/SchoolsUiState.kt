package com.teddy.mirandaytoledo.catalog.presentation.schools

import com.teddy.mirandaytoledo.catalog.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.domain.School
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface SchoolsUiState {
    data object Loading : SchoolsUiState
    data class Success(
        val schools: List<School> = emptyList(),
        val educationalLevels: List<EducationalLevel> = emptyList(),
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val currentPage: Int = 1,
        val searchQuery: String = "",
        val selectedLevelId: Int? = null,
        val isLoadingLevels: Boolean = false
    ) : SchoolsUiState

    data class Error(val error: NetworkError) : SchoolsUiState
}
