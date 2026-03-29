package com.teddy.mirandaytoledo.catalog.frames.sizes.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSizeRequest(
    val name: String,
    @SerialName("sizeGroup") val sizeGroup: String
)
