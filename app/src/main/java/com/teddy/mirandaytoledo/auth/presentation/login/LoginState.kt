package com.teddy.mirandaytoledo.auth.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val isLoading: Boolean = false,
    val username : String = "",
    val password:String = "",
    val isSuccess: Boolean = false
)
