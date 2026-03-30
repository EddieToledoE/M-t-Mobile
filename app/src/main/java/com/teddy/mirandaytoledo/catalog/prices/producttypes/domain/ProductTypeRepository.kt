package com.teddy.mirandaytoledo.catalog.prices.producttypes.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface ProductTypeRepository {
    suspend fun getAll(): Result<List<ProductType>, NetworkError>
    suspend fun create(
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ): Result<ProductType, NetworkError>
    suspend fun update(
        id: Int,
        name: String,
        requiresSize: Boolean,
        requiresFinish: Boolean,
        requiresFrameModel: Boolean,
        requiresColor: Boolean,
        allowedSizeGroup: String?
    ): Result<ProductType, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
