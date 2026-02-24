package com.teddy.mirandaytoledo.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String,
    val password: String
)


