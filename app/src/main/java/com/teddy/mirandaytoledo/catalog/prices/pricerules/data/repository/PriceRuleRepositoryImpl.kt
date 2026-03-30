package com.teddy.mirandaytoledo.catalog.prices.pricerules.data.repository

import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.dto.CreatePriceRuleRequest
import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.dto.UpdatePriceRuleRequest
import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.networking.PriceRuleRemoteDataSource
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRuleRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class PriceRuleRepositoryImpl(
    private val remoteDataSource: PriceRuleRemoteDataSource
) : PriceRuleRepository {

    override suspend fun getAll(): Result<List<PriceRule>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ): Result<PriceRule, NetworkError> {
        return remoteDataSource.create(
            CreatePriceRuleRequest(
                productTypeId = productTypeId,
                finishId = finishId,
                sizeId = sizeId,
                price = price
            )
        ).map { it.toDomain() }
    }

    override suspend fun update(
        id: Int,
        productTypeId: Int,
        finishId: Int,
        sizeId: Int,
        price: Double
    ): Result<PriceRule, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdatePriceRuleRequest(
                productTypeId = productTypeId,
                finishId = finishId,
                sizeId = sizeId,
                price = price
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id)
    }
}
