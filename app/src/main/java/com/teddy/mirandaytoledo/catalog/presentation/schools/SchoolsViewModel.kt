package com.teddy.mirandaytoledo.catalog.presentation.schools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.domain.usecase.CreateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.DeleteSchoolUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolsByLevelUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.UpdateSchoolUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SchoolsViewModel(
    private val getSchoolsUseCase: GetSchoolsUseCase,
    private val getSchoolsByLevelUseCase: GetSchoolsByLevelUseCase,
    private val createSchoolUseCase: CreateSchoolUseCase,
    private val updateSchoolUseCase: UpdateSchoolUseCase,
    private val deleteSchoolUseCase: DeleteSchoolUseCase,
    private val getEducationalLevelsUseCase: GetEducationalLevelsUseCase
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    private val _uiState = MutableStateFlow<SchoolsUiState>(SchoolsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var isLoadingInProgress = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = SchoolsUiState.Loading
            
            // Load educational levels
            getEducationalLevelsUseCase()
                .onSuccess { levels ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolsUiState.Success) {
                            currentState.copy(educationalLevels = levels, isLoadingLevels = false)
                        } else {
                            SchoolsUiState.Success(educationalLevels = levels, isLoadingLevels = false)
                        }
                    }
                }
                .onError { error ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolsUiState.Success) {
                            currentState
                        } else {
                            SchoolsUiState.Error(error)
                        }
                    }
                }

            // Load schools
            loadSchools(reset = true)
        }
    }

    fun loadSchools(reset: Boolean = false) {
        if (isLoadingInProgress) return

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is SchoolsUiState.Success) return@launch

            val page = if (reset) 1 else currentState.currentPage
            val selectedLevelId = currentState.selectedLevelId
            val searchQuery = currentState.searchQuery

            isLoadingInProgress = true
            _uiState.update {
                if (it is SchoolsUiState.Success) {
                    it.copy(isLoadingMore = true, currentPage = page)
                } else {
                    SchoolsUiState.Success(isLoadingMore = true, currentPage = page)
                }
            }

            val result = if (selectedLevelId != null) {
                getSchoolsByLevelUseCase(
                    educationalLevelId = selectedLevelId,
                    page = page,
                    pageSize = PAGE_SIZE,
                    search = searchQuery.ifBlank { null }
                )
            } else {
                getSchoolsUseCase(
                    page = page,
                    pageSize = PAGE_SIZE,
                    search = searchQuery.ifBlank { null }
                )
            }

            result
                .onSuccess { newSchools ->
                    _uiState.update { currentState ->
                        if (currentState is SchoolsUiState.Success) {
                            val schools = if (reset) {
                                newSchools
                            } else {
                                (currentState.schools + newSchools).distinctBy { it.id }
                            }
                            currentState.copy(
                                schools = schools,
                                isLoadingMore = false,
                                hasMoreData = newSchools.size == PAGE_SIZE,
                                currentPage = page + 1
                            )
                        } else {
                            SchoolsUiState.Success(
                                schools = newSchools,
                                educationalLevels = (currentState as? SchoolsUiState.Success)?.educationalLevels
                                    ?: emptyList(),
                                hasMoreData = newSchools.size == PAGE_SIZE,
                                currentPage = page + 1
                            )
                        }
                    }
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolsUiState.Success) {
                            it.copy(isLoadingMore = false)
                        } else {
                            SchoolsUiState.Error(error)
                        }
                    }
                }

            isLoadingInProgress = false
        }
    }

    fun onSearchChanged(query: String) {
        searchJob?.cancel()
        
        _uiState.update { currentState ->
            if (currentState is SchoolsUiState.Success) {
                currentState.copy(searchQuery = query)
            } else {
                SchoolsUiState.Success(searchQuery = query)
            }
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            loadSchools(reset = true)
        }
    }

    fun onEducationalLevelFilterChanged(levelId: Int?) {
        _uiState.update { currentState ->
            if (currentState is SchoolsUiState.Success) {
                currentState.copy(selectedLevelId = levelId)
            } else {
                SchoolsUiState.Success(selectedLevelId = levelId)
            }
        }
        loadSchools(reset = true)
    }

    fun createSchool(educationalLevelId: Int, name: String) {
        viewModelScope.launch {
            createSchoolUseCase(
                educationalLevelId = educationalLevelId,
                name = name.trim()
            )
                .onSuccess {
                    loadSchools(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolsUiState.Success) {
                            it
                        } else {
                            SchoolsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun updateSchool(id: Int, name: String, isActive: Boolean) {
        viewModelScope.launch {
            updateSchoolUseCase(
                id = id,
                name = name.trim(),
                isActive = isActive
            )
                .onSuccess {
                    loadSchools(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolsUiState.Success) {
                            it
                        } else {
                            SchoolsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun deleteSchool(id: Int) {
        viewModelScope.launch {
            deleteSchoolUseCase(id = id)
                .onSuccess {
                    loadSchools(reset = true)
                }
                .onError { error ->
                    _uiState.update {
                        if (it is SchoolsUiState.Success) {
                            it
                        } else {
                            SchoolsUiState.Error(error)
                        }
                    }
                }
        }
    }

    fun loadMoreIfNeeded(lastVisibleIndex: Int, totalCount: Int) {
        val currentState = _uiState.value
        if (currentState !is SchoolsUiState.Success) return

        val threshold = totalCount - 5
        if (lastVisibleIndex >= threshold && currentState.hasMoreData && !currentState.isLoadingMore) {
            loadSchools(reset = false)
        }
    }

    fun retryLoad() {
        loadSchools(reset = true)
    }
}
