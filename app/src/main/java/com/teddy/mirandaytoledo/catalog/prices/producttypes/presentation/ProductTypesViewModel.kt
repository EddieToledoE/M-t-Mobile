package com.teddy.mirandaytoledo.catalog.prices.producttypes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.CreateProductTypeUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.DeleteProductTypeUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.GetProductTypesUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.UpdateProductTypeUseCase
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductTypesViewModel(
    private val getProductTypesUseCase: GetProductTypesUseCase,
    private val createProductTypeUseCase: CreateProductTypeUseCase,
    private val updateProductTypeUseCase: UpdateProductTypeUseCase,
    private val deleteProductTypeUseCase: DeleteProductTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductTypesUiState>(ProductTypesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProductTypes()
    }

    fun loadProductTypes() {
        viewModelScope.launch {
            _uiState.value = ProductTypesUiState.Loading
            getProductTypesUseCase()
                .onSuccess { productTypes ->
                    _uiState.value = ProductTypesUiState.Success(productTypes)
                }
                .onError { error ->
                    _uiState.value = ProductTypesUiState.Error(error)
                }
        }
    }

    fun createProductType(
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ) {
        viewModelScope.launch {
            createProductTypeUseCase(
                name = name.trim(),
                requiresSize = requiresSize,
                requiresFinish = requiresFinish,
                requiresFrameModel = requiresFrameModel,
                requiresColor = requiresColor,
                allowedSizeGroup = allowedSizeGroup?.trim()?.lowercase()
            )
                .onSuccess { loadProductTypes() }
                .onError { error ->
                    _uiState.value = ProductTypesUiState.Error(error)
                }
        }
    }

    fun updateProductType(
        id: Int,
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ) {
        viewModelScope.launch {
            updateProductTypeUseCase(
                id = id,
                name = name.trim(),
                requiresSize = requiresSize,
                requiresFinish = requiresFinish,
                requiresFrameModel = requiresFrameModel,
                requiresColor = requiresColor,
                allowedSizeGroup = allowedSizeGroup?.trim()?.lowercase()
            )
                .onSuccess { loadProductTypes() }
                .onError { error ->
                    _uiState.value = ProductTypesUiState.Error(error)
                }
        }
    }

    fun deleteProductType(id: Int) {
        viewModelScope.launch {
            deleteProductTypeUseCase(id = id)
                .onSuccess { loadProductTypes() }
                .onError { error ->
                    _uiState.value = ProductTypesUiState.Error(error)
                }
        }
    }
}
