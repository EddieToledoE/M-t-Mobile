package com.teddy.mirandaytoledo.catalog.scholar.groups.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.CreateSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.DeleteSchoolGroupUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsBySchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.UpdateSchoolGroupUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        private const val PAGE_SIZE = 20
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    private val _uiState = MutableStateFlow<SchoolGroupsUiState>(SchoolGroupsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var isLoadingInProgress = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = SchoolGroupsUiState.Loading

            // Load schools for filter
            getSchoolsUseCase(page = 1, pageSize = 100, search = null)
                .onSuccess { schools ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            currentState.copy(schools = schools, isLoadingSchools = false)
                        } else {
                            SchoolGroupsUiState.Success(schools = schools, isLoadingSchools = false)
                        }
                    }
                }
                .onError { _ ->
                    // Continue even if schools fail to load
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            currentState.copy(isLoadingSchools = false)
                        } else {
                            SchoolGroupsUiState.Success(isLoadingSchools = false)
                        }
                    }
                }

            // Load school groups
            loadSchoolGroups(reset = true)
        }
    }

    fun loadSchoolGroups(reset: Boolean = false) {
        if (isLoadingInProgress) return

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is SchoolGroupsUiState.Success) return@launch

            val page = if (reset) 1 else currentState.currentPage
            val selectedSchoolId = currentState.selectedSchoolId
            val searchQuery = currentState.searchQuery

            isLoadingInProgress = true
            _uiState.update {
                if (it is SchoolGroupsUiState.Success) {
                    it.copy(isLoadingMore = true, currentPage = page)
                } else {
                    SchoolGroupsUiState.Success(isLoadingMore = true, currentPage = page)
                }
            }

            val result = if (selectedSchoolId != null) {
                getSchoolGroupsBySchoolUseCase(
                    schoolId = selectedSchoolId,
                    page = page,
                    pageSize = PAGE_SIZE,
                    search = searchQuery.ifBlank { null }
                )
            } else {
                getSchoolGroupsUseCase(
                    page = page,
                    pageSize = PAGE_SIZE,
                    search = searchQuery.ifBlank { null }
                )
            }

            result
                .onSuccess { newGroups ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolGroupsUiState.Success) {
                            val groups = if (reset) {
                                newGroups
                            } else {
                                (currentState.schoolGroups + newGroups).distinctBy { it.id }
                            }
                            currentState.copy(
                                schoolGroups = groups,
                                isLoadingMore = false,
                                hasMoreData = newGroups.size == PAGE_SIZE,
                                currentPage = page + 1
                            )
                        } else {
                            SchoolGroupsUiState.Success(
                                schoolGroups = newGroups,
                                schools = (currentState as? SchoolGroupsUiState.Success)?.schools
                                    ?: emptyList(),
                                hasMoreData = newGroups.size == PAGE_SIZE,
                                currentPage = page + 1
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
        searchJob?.cancel()

        _uiState.update { currentState ->
            if (currentState is SchoolGroupsUiState.Success) {
                currentState.copy(searchQuery = query)
            } else {
                SchoolGroupsUiState.Success(searchQuery = query)
            }
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            loadSchoolGroups(reset = true)
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
        val currentState = _uiState.value
        if (currentState !is SchoolGroupsUiState.Success) return

        val threshold = totalCount - 5
        if (lastVisibleIndex >= threshold && currentState.hasMoreData && !currentState.isLoadingMore) {
            loadSchoolGroups(reset = false)
        }
    }

    fun retryLoad() {
        loadSchoolGroups(reset = true)
    }
}
