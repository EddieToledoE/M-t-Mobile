package com.teddy.mirandaytoledo.auth.domain

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface LoginDataSource {
    suspend fun doLogin(user: User): Result<Boolean, NetworkError>
}