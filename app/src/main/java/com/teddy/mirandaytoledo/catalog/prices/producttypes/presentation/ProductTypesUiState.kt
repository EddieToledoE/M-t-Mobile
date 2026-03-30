package com.teddy.mirandaytoledo.catalog.prices.producttypes.presentation

import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface ProductTypesUiState {
    data object Loading : ProductTypesUiState
    data class Success(val productTypes: List<ProductType>) : ProductTypesUiState
    data class Error(val error: NetworkError) : ProductTypesUiState
}
