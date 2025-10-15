package com.teddy.mirandaytoledo.auth.data.networking

import com.teddy.mirandaytoledo.auth.data.mappers.toDto
import com.teddy.mirandaytoledo.auth.domain.LoginDataSource
import com.teddy.mirandaytoledo.auth.domain.User
import com.teddy.mirandaytoledo.core.data.networking.constructUrl
import com.teddy.mirandaytoledo.core.data.networking.safeCall
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteLoginDataSource(private val httpClient: HttpClient) : LoginDataSource {

    override suspend fun doLogin(user: User): Result<Boolean, NetworkError> {
        return safeCall<String> {
            httpClient.post(
                urlString = constructUrl("/login"),
                block = { setBody(user.toDto()) })
        }.map { string ->
           when{
               string.contains("exitoso", ignoreCase = true) -> true
               string.contains("incorrectos", ignoreCase = true) -> false
               else -> false
           }
        }
    }
}