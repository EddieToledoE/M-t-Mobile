package com.teddy.mirandaytoledo.catalog.prices.producttypes.data.repository

import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.CreateProductTypeRequest
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.UpdateProductTypeRequest
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.networking.ProductTypeRemoteDataSource
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductType
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.ProductTypeRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class ProductTypeRepositoryImpl(
    private val remoteDataSource: ProductTypeRemoteDataSource
) : ProductTypeRepository {

    override suspend fun getAll(): Result<List<ProductType>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ): Result<ProductType, NetworkError> {
        return remoteDataSource.create(
            CreateProductTypeRequest(
                name = name,
                requiresSize = requiresSize,
                requiresFinish = requiresFinish,
                requiresFrameModel = requiresFrameModel,
                requiresColor = requiresColor,
                allowedSizeGroup = allowedSizeGroup
            )
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ): Result<ProductType, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateProductTypeRequest(
                name = name,
                requiresSize = requiresSize,
                requiresFinish = requiresFinish,
                requiresFrameModel = requiresFrameModel,
                requiresColor = requiresColor,
                allowedSizeGroup = allowedSizeGroup
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
