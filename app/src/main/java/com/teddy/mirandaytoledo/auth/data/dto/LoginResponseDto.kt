package com.teddy.mirandaytoledo.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token : String,
    val username : String,
    val expiresAt : String
)