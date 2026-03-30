package com.teddy.mirandaytoledo.catalog.prices.pricerules.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface PriceRuleRepository {
    suspend fun getAll(): Result<List<PriceRule>, NetworkError>
    suspend fun create(
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ): Result<PriceRule, NetworkError>
    suspend fun update(
        id: Int,
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ): Result<PriceRule, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
