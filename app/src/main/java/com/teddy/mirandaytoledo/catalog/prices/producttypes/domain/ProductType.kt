package com.teddy.mirandaytoledo.catalog.prices.producttypes.domain

data class ProductType(
    val id: Int,
    val name: String,
    val requiresSize: Boolean,
    val requiresFinish: Boolean,
    val requiresFrameModel: Boolean,
    val requiresColor: Boolean,
    val allowedSizeGroup: String?,
    val isActive: Boolean
)
