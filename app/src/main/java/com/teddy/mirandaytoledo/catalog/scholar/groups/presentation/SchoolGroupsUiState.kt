package com.teddy.mirandaytoledo.catalog.scholar.groups.presentation

import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface SchoolGroupsUiState {
    data object Loading : SchoolGroupsUiState
    data class Success(
        val schoolGroups: List<SchoolGroup> = emptyList(),
        val schools: List<School> = emptyList(),
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val currentPage: Int = 1,
        val searchQuery: String = "",
        val selectedSchoolId: Int? = null,
        val isLoadingSchools: Boolean = false
    ) : SchoolGroupsUiState

    data class Error(val error: NetworkError) : SchoolGroupsUiState
}
