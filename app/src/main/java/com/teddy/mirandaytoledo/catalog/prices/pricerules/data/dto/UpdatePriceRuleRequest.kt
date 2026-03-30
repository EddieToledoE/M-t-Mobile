package com.teddy.mirandaytoledo.catalog.prices.pricerules.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePriceRuleRequest(
    val productTypeId: Int,
    val finishId: Int,
    val sizeId: Int,
    val price: Double
)
