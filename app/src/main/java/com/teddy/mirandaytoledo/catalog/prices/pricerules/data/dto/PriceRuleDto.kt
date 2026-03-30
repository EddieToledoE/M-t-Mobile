package com.teddy.mirandaytoledo.catalog.prices.pricerules.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PriceRuleDto(
    val id: Int,
    val productTypeId: Int,
    val productTypeName: String,
    val finishId: Int,
    val finishName: String,
    val sizeId: Int,
    val sizeName: String,
    val sizeGroup: String,
    val price: Double,
    val isActive: Boolean
)
