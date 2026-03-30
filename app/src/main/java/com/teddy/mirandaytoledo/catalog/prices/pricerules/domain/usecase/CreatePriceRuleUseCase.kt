package com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRuleRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreatePriceRuleUseCase(
    private val repository: PriceRuleRepository
) {
    suspend operator fun invoke(
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ): Result<PriceRule, NetworkError> {
        return repository.create(
            productTypeId = productTypeId,
            finishId = finishId,
            sizeId = sizeId,
            price = price
        )
    }
}
