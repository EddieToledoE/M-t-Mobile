package com.teddy.mirandaytoledo.catalog.presentation.educationalLevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.domain.usecase.CreateEducationalLevelUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.DeleteEducationalLevelUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.domain.usecase.UpdateEducationalLevelUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EducationalLevelsViewModel(
    private val getEducationalLevelsUseCase: GetEducationalLevelsUseCase,
    private val createEducationalLevelUseCase: CreateEducationalLevelUseCase,
    private val updateEducationalLevelUseCase: UpdateEducationalLevelUseCase,
    private val deleteEducationalLevelUseCase: DeleteEducationalLevelUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EducationalLevelsUiState>(EducationalLevelsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadLevels()
    }

    fun loadLevels() {
        viewModelScope.launch {
            _uiState.value = EducationalLevelsUiState.Loading
            getEducationalLevelsUseCase()
                .onSuccess { levels ->
                    _uiState.value = EducationalLevelsUiState.Success(levels)
                }
                .onError { error ->
                    _uiState.value = EducationalLevelsUiState.Error(error)
                }
        }
    }

    fun createLevel(name: String, maxGrade: Int) {
        viewModelScope.launch {
            createEducationalLevelUseCase(name = name.trim(), maxGrade = maxGrade)
                .onSuccess { loadLevels() }
                .onError { error ->
                    _uiState.value = EducationalLevelsUiState.Error(error)
                }
        }
    }

    fun updateLevel(id: Int, name: String, maxGrade: Int, isActive: Boolean) {
        viewModelScope.launch {
            updateEducationalLevelUseCase(
                id = id,
                name = name.trim(),
                maxGrade = maxGrade,
                isActive = isActive
            )
                .onSuccess { loadLevels() }
                .onError { error ->
                    _uiState.value = EducationalLevelsUiState.Error(error)
                }
        }
    }

    fun deleteLevel(id: Int) {
        viewModelScope.launch {
            deleteEducationalLevelUseCase(id = id)
                .onSuccess { loadLevels() }
                .onError { error ->
                    _uiState.value = EducationalLevelsUiState.Error(error)
                }
        }
    }
}
