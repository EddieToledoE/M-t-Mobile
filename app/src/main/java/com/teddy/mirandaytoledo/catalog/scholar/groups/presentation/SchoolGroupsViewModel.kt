package com.teddy.mirandaytoledo.catalog.scholar.groups.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.CreateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.DeleteSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsBySchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.SchoolGroup
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.UpdateSchoolGroupUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SchoolGroupsViewModel(
    private val getSchoolGroupsUseCase: GetSchoolGroupsUseCase,
    private val getSchoolGroupsBySchoolUseCase: GetSchoolGroupsBySchoolUseCase,
    private val createSchoolGroupUseCase: CreateSchoolGroupUseCase,
    private val updateSchoolGroupUseCase: UpdateSchoolGroupUseCase,
    private val deleteSchoolGroupUseCase: DeleteSchoolGroupUseCase,
    private val getSchoolsUseCase: GetSchoolsUseCase
) : ViewModel() {

    companion object {
        private const val FETCH_LIMIT = 1000
    }

    private val _uiState = MutableStateFlow<SchoolGroupsUiState>(SchoolGroupsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var isLoadingInProgress = false
    private var sourceGroups: List<SchoolGroup> = emptyList()
    private var cachedSchools: List<School> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = SchoolGroupsUiState.Loading

            getSchoolsUseCase(page = 1, pageSize = 100, search = null)
                .onSuccess { schools ->
                    cachedSchools = schools
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            currentState.copy(schools = schools, isLoadingSchools = false)
                        } else {
                            SchoolGroupsUiState.Success(schools = schools, isLoadingSchools = false)
                        }
                    }
                }
                .onError { _ ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            currentState.copy(isLoadingSchools = false)
                        } else {
                            SchoolGroupsUiState.Success(isLoadingSchools = false)
                        }
                    }
                }

            loadSchoolGroups(reset = true)
        }
    }

    fun loadSchoolGroups(reset: Boolean = false) {
        if (isLoadingInProgress) return

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is SchoolGroupsUiState.Success) return@launch

            val selectedSchoolId = currentState.selectedSchoolId

            isLoadingInProgress = true
            _uiState.update {
                if (it is SchoolGroupsUiState.Success) {
                    it.copy(isLoadingMore = true)
                } else {
                    SchoolGroupsUiState.Success(
                        schools = cachedSchools,
                        isLoadingMore = true
                    )
                }
            }

            val result = if (selectedSchoolId != null) {
                getSchoolGroupsBySchoolUseCase(
                    schoolId = selectedSchoolId,
                    page = 1,
                    pageSize = FETCH_LIMIT,
                    search = null
                )
            } else {
                getSchoolGroupsUseCase(
                    page = 1,
                    pageSize = FETCH_LIMIT,
                    search = null
                )
            }

            result
                .onSuccess { newGroups ->
                    sourceGroups = newGroups
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            currentState.copy(
                                schoolGroups = applySearchFilter(
                                    groups = sourceGroups,
                                    query = currentState.searchQuery
                                ),
                                schools = cachedSchools,
                                isLoadingMore = false,
                                hasMoreData = false,
                                currentPage = 1
                            )
                        } else {
                            SchoolGroupsUiState.Success(
                                schoolGroups = applySearchFilter(sourceGroups, ""),
                                schools = cachedSchools,
                                hasMoreData = false,
                                currentPage = 1
                            )
                        }
                    }
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolGroupsUiState.Success) {
                            it.copy(isLoadingMore = false)
                        } else {
                            SchoolGroupsUiState.Error(error)
                        }
                    }
                }

            isLoadingInProgress = false
        }
    }

    fun onSearchChanged(query: String) {
        _uiState.update { currentState ->
            if (currentState is SchoolGroupsUiState.Success) {
                currentState.copy(
                    searchQuery = query,
                    schoolGroups = applySearchFilter(sourceGroups, query)
                )
            } else {
                SchoolGroupsUiState.Success(
                    searchQuery = query,
                    schools = cachedSchools
                )
            }
        }
    }

    fun onSchoolFilterChanged(schoolId: Int?) {
        _uiState.update { currentState ->
            if (currentState is SchoolGroupsUiState.Success) {
                currentState.copy(selectedSchoolId = schoolId)
            } else {
                SchoolGroupsUiState.Success(selectedSchoolId = schoolId)
            }
        }
        loadSchoolGroups(reset = true)
    }

    fun createSchoolGroup(schoolId: Int, groupCode: String) {
        viewModelScope.launch {
            createSchoolGroupUseCase(
                schoolId = schoolId,
                groupCode = groupCode.trim()
            )
                .onSuccess {
                    loadSchoolGroups(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolGroupsUiState.Success) {
                            it
                        } else {
                            SchoolGroupsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun updateSchoolGroup(id: Int, groupCode: String, isActive: Boolean) {
        viewModelScope.launch {
            updateSchoolGroupUseCase(
                id = id,
                groupCode = groupCode.trim(),
                isActive = isActive
            )
                .onSuccess {
                    loadSchoolGroups(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolGroupsUiState.Success) {
                            it
                        } else {
                            SchoolGroupsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun deleteSchoolGroup(id: Int) {
        viewModelScope.launch {
            deleteSchoolGroupUseCase(id = id)
                .onSuccess {
                    loadSchoolGroups(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolGroupsUiState.Success) {
                            it
                        } else {
                            SchoolGroupsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun loadMoreIfNeeded(lastVisibleIndex: Int, totalCount: Int) {
        return
    }

    fun retryLoad() {
        loadSchoolGroups(reset = true)
    }

    private fun applySearchFilter(
        groups: List<SchoolGroup>,
        query: String
    ): List<SchoolGroup> {
        val term = query.trim()
        if (term.isBlank()) return groups

        return groups.filter { group ->
            group.groupCode.contains(term, ignoreCase = true) ||
                (group.schoolName?.contains(term, ignoreCase = true) == true) ||
                (group.educationalLevelName?.contains(term, ignoreCase = true) == true)
        }
    }
}
