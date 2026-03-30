package com.teddy.mirandaytoledo.catalog.prices.pricerules.data.mappers

import com.teddy.mirandaytoledo.catalog.prices.pricerules.data.dto.PriceRuleDto
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.PriceRule

fun PriceRuleDto.toDomain(): PriceRule {
    return PriceRule(
        id = id,
        productTypeId = productTypeId,
        productTypeName = productTypeName,
        finishId = finishId,
        finishName = finishName,
        sizeId = sizeId,
        sizeName = sizeName,
        sizeGroup = sizeGroup,
        price = price,
        isActive = isActive
    )
}
