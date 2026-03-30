package com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductTypeRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class GetProductTypesUseCase(
    private val repository: ProductTypeRepository
) {
    suspend operator fun invoke(): Result<List<ProductType>, NetworkError> {
        return repository.getAll()
    }
}
