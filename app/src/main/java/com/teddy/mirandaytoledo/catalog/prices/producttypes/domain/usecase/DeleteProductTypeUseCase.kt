package com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductTypeRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

class DeleteProductTypeUseCase(
    private val repository: ProductTypeRepository
) {
    suspend operator fun invoke(id: Int): EmptyResult<NetworkError> {
        return repository.delete(id = id)
    }
}
