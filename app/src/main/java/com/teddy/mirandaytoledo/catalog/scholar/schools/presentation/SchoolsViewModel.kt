package com.teddy.mirandaytoledo.catalog.scholar.schools.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.EducationalLevel
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.CreateSchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.DeleteSchoolUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.School
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsByLevelUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.UpdateSchoolUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
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
        private const val FETCH_LIMIT = 1000
    }

    private val _uiState = MutableStateFlow<SchoolsUiState>(SchoolsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var isLoadingInProgress = false
    private var sourceSchools: List<School> = emptyList()
    private var cachedLevels: List<EducationalLevel> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = SchoolsUiState.Loading
            
            getEducationalLevelsUseCase()
                .onSuccess { levels ->
                    cachedLevels = levels
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
                        currentState as? SchoolsUiState.Success ?: SchoolsUiState.Error(error)
                    }
                }

            loadSchools(reset = true)
        }
    }

    fun loadSchools(reset: Boolean = false) {
        if (isLoadingInProgress) return

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is SchoolsUiState.Success) return@launch

            val selectedLevelId = currentState.selectedLevelId

            isLoadingInProgress = true
            _uiState.update {
                if (it is SchoolsUiState.Success) {
                    it.copy(isLoadingMore = true)
                } else {
                    SchoolsUiState.Success(
                        educationalLevels = cachedLevels,
                        isLoadingMore = true
                    )
                }
            }

            val result = if (selectedLevelId != null) {
                getSchoolsByLevelUseCase(
                    educationalLevelId = selectedLevelId,
                    page = 1,
                    pageSize = FETCH_LIMIT,
                    search = null
                )
            } else {
                getSchoolsUseCase(
                    page = 1,
                    pageSize = FETCH_LIMIT,
                    search = null
                )
            }

            result
                .onSuccess { newSchools ->
                    sourceSchools = newSchools
                    _uiState.update { currentState ->
                        if (currentState is SchoolsUiState.Success) {
                            currentState.copy(
                                schools = applySearchFilter(
                                    schools = sourceSchools,
                                    query = currentState.searchQuery
                                ),
                                educationalLevels = cachedLevels,
                                isLoadingMore = false,
                                hasMoreData = false,
                                currentPage = 1
                            )
                        } else {
                            SchoolsUiState.Success(
                                schools = applySearchFilter(sourceSchools, ""),
                                educationalLevels = cachedLevels,
                                hasMoreData = false,
                                currentPage = 1
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
        _uiState.update { currentState ->
            if (currentState is SchoolsUiState.Success) {
                currentState.copy(
                    searchQuery = query,
                    schools = applySearchFilter(sourceSchools, query)
                )
            } else {
                SchoolsUiState.Success(
                    searchQuery = query,
                    educationalLevels = cachedLevels
                )
            }
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
        return
    }

    fun retryLoad() {
        loadSchools(reset = true)
    }

    private fun applySearchFilter(
        schools: List<School>,
        query: String
    ): List<School> {
        val term = query.trim()
        if (term.isBlank()) return schools

        return schools.filter { school ->
            school.name.contains(term, ignoreCase = true) ||
                    school.educationalLevelName?.contains(term, ignoreCase = true) == true
        }
    }
}
