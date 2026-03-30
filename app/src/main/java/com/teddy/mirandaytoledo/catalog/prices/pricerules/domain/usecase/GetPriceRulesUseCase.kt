package com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRuleRepository

class GetPriceRulesUseCase(
    private val repository: PriceRuleRepository
) {
    suspend operator fun invoke() = repository.getAll()
}
