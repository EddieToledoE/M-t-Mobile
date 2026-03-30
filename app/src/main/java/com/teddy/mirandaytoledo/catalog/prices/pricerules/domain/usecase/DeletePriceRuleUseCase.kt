package com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRuleRepository

class DeletePriceRuleUseCase(
    private val repository: PriceRuleRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}
