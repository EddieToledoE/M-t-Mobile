package com.teddy.mirandaytoledo.catalog.frames.finishes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.CreateFinishUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.DeleteFinishUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.UpdateFinishUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishesViewModel(
    private val getFinishesUseCase: GetFinishesUseCase,
    private val createFinishUseCase: CreateFinishUseCase,
    private val updateFinishUseCase: UpdateFinishUseCase,
    private val deleteFinishUseCase: DeleteFinishUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FinishesUiState>(FinishesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFinishes()
    }

    fun loadFinishes() {
        viewModelScope.launch {
            _uiState.value = FinishesUiState.Loading
            getFinishesUseCase()
                .onSuccess { finishes ->
                    _uiState.value = FinishesUiState.Success(finishes)
                }
                .onError { error ->
                    _uiState.value = FinishesUiState.Error(error)
                }
        }
    }

    fun createFinish(name: String) {
        viewModelScope.launch {
            createFinishUseCase(name = name.trim())
                .onSuccess { loadFinishes() }
                .onError { error ->
                    _uiState.value = FinishesUiState.Error(error)
                }
        }
    }

    fun updateFinish(id: Int, name: String, isActive: Boolean) {
        viewModelScope.launch {
            updateFinishUseCase(id = id, name = name.trim(), isActive = isActive)
                .onSuccess { loadFinishes() }
                .onError { error ->
                    _uiState.value = FinishesUiState.Error(error)
                }
        }
    }

    fun deleteFinish(id: Int) {
        viewModelScope.launch {
            deleteFinishUseCase(id = id)
                .onSuccess { loadFinishes() }
                .onError { error ->
                    _uiState.value = FinishesUiState.Error(error)
                }
        }
    }
}
