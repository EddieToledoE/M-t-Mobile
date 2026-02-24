package com.teddy.mirandaytoledo.auth.data.networking

import android.util.Log
import com.teddy.mirandaytoledo.auth.data.dto.LoginResponseDto
import com.teddy.mirandaytoledo.auth.data.mappers.toDto
import com.teddy.mirandaytoledo.auth.domain.LoginDataSource
import com.teddy.mirandaytoledo.auth.domain.User
import com.teddy.mirandaytoledo.core.data.networking.constructUrl
import com.teddy.mirandaytoledo.core.data.networking.safeCall
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteLoginDataSource(private val httpClient: HttpClient) : LoginDataSource {

    override suspend fun doLogin(user: User): Result<LoginResponseDto, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("/api/Auth/login")
            ) {
                setBody(user.toDto())
            }
        }
    }
}
