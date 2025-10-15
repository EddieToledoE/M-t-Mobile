package com.teddy.mirandaytoledo.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val usuario: String,
    val contra: String
)


