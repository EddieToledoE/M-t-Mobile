package com.teddy.mirandaytoledo.catalog.prices.pricerules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.GetSizesUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.CreatePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.DeletePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.GetPriceRulesUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.UpdatePriceRuleUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.GetProductTypesUseCase
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PriceRulesViewModel(
    private val getPriceRulesUseCase: GetPriceRulesUseCase,
    private val getProductTypesUseCase: GetProductTypesUseCase,
    private val getFinishesUseCase: GetFinishesUseCase,
    private val getSizesUseCase: GetSizesUseCase,
    private val createPriceRuleUseCase: CreatePriceRuleUseCase,
    private val updatePriceRuleUseCase: UpdatePriceRuleUseCase,
    private val deletePriceRuleUseCase: DeletePriceRuleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PriceRulesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val productTypesResult = getProductTypesUseCase()) {
                is Result.Error -> {
                    setError(productTypesResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(productTypes = productTypesResult.data)
                }
            }

            when (val finishesResult = getFinishesUseCase()) {
                is Result.Error -> {
                    setError(finishesResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(finishes = finishesResult.data)
                }
            }

            when (val sizesResult = getSizesUseCase()) {
                is Result.Error -> {
                    setError(sizesResult.error)
                    return@launch
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(sizes = sizesResult.data)
                }
            }

            when (val priceRulesResult = getPriceRulesUseCase()) {
                is Result.Error -> setError(priceRulesResult.error)
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        priceRules = priceRulesResult.data
                    )
                }
            }
        }
    }

    fun createPriceRule(
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ) {
        viewModelScope.launch {
            createPriceRuleUseCase(
                productTypeId = productTypeId,
                finishId = finishId,
                sizeId = sizeId,
                price = price
            )
                .onSuccess { loadAll() }
                .onError { error -> _uiState.value = _uiState.value.copy(error = error) }
        }
    }

    fun updatePriceRule(
        id: Int,
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ) {
        viewModelScope.launch {
            updatePriceRuleUseCase(
                id = id,
                productTypeId = productTypeId,
                finishId = finishId,
                sizeId = sizeId,
                price = price
            )
                .onSuccess { loadAll() }
                .onError { error -> _uiState.value = _uiState.value.copy(error = error) }
        }
    }

    fun deletePriceRule(id: Int) {
        viewModelScope.launch {
            deletePriceRuleUseCase(id)
                .onSuccess { loadAll() }
                .onError { error -> _uiState.value = _uiState.value.copy(error = error) }
        }
    }

    private fun setError(error: NetworkError) {
        _uiState.value = _uiState.value.copy(isLoading = false, error = error)
    }
}
