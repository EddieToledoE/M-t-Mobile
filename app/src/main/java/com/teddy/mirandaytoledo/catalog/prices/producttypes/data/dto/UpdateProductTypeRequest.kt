package com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProductTypeRequest(
    val name: String,
    val requiresSize: Boolean,
    val requiresFinish: Boolean,
    val requiresFrameModel: Boolean,
    val requiresColor: Boolean,
    val allowedSizeGroup: String?
)
