package com.teddy.mirandaytoledo.auth.presentation.login

import com.teddy.mirandaytoledo.core.domain.util.NetworkError

sealed interface LoginEvents {
    data class Error(val error: NetworkError) : LoginEvents
}