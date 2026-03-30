package com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase

import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductTypeRepository
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

class CreateProductTypeUseCase(
    private val repository: ProductTypeRepository
) {
    suspend operator fun invoke(
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ): Result<ProductType, NetworkError> {
        return repository.create(
            name = name,
            requiresSize = requiresSize,
            requiresFinish = requiresFinish,
            requiresFrameModel = requiresFrameModel,
            requiresColor = requiresColor,
            allowedSizeGroup = allowedSizeGroup
        )
    }
}
