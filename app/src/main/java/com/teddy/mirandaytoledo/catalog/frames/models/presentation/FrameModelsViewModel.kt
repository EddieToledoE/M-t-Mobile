package com.teddy.mirandaytoledo.catalog.frames.models.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.CreateFrameModelUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.DeleteFrameModelUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.GetFrameModelsUseCase
import com.teddy.mirandaytoledo.catalog.frames.models.domain.usecase.UpdateFrameModelUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FrameModelsViewModel(
    private val getFrameModelsUseCase: GetFrameModelsUseCase,
    private val createFrameModelUseCase: CreateFrameModelUseCase,
    private val updateFrameModelUseCase: UpdateFrameModelUseCase,
    private val deleteFrameModelUseCase: DeleteFrameModelUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FrameModelsUiState>(FrameModelsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadModels()
    }

    fun loadModels() {
        viewModelScope.launch {
            _uiState.value = FrameModelsUiState.Loading
            getFrameModelsUseCase()
                .onSuccess { models ->
                    _uiState.value = FrameModelsUiState.Success(models)
                }
                .onError { error ->
                    _uiState.value = FrameModelsUiState.Error(error)
                }
        }
    }

    fun createModel(name: String) {
        viewModelScope.launch {
            createFrameModelUseCase(name = name.trim())
                .onSuccess { loadModels() }
                .onError { error ->
                    _uiState.value = FrameModelsUiState.Error(error)
                }
        }
    }

    fun updateModel(id: Int, name: String) {
        viewModelScope.launch {
            updateFrameModelUseCase(id = id, name = name.trim())
                .onSuccess { loadModels() }
                .onError { error ->
                    _uiState.value = FrameModelsUiState.Error(error)
                }
        }
    }

    fun deleteModel(id: Int) {
        viewModelScope.launch {
            deleteFrameModelUseCase(id = id)
                .onSuccess { loadModels() }
                .onError { error ->
                    _uiState.value = FrameModelsUiState.Error(error)
                }
        }
    }
}
