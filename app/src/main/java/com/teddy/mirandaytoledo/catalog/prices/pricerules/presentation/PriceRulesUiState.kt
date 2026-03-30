package com.teddy.mirandaytoledo.catalog.prices.pricerules.presentation

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.Finish
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.Size
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

data class PriceRulesUiState(
    val isLoading: Boolean = true,
    val error: NetworkError? = null,
    val priceRules: List<PriceRule> = emptyList(),
    val productTypes: List<ProductType> = emptyList(),
    val finishes: List<Finish> = emptyList(),
    val sizes: List<Size> = emptyList()
)
