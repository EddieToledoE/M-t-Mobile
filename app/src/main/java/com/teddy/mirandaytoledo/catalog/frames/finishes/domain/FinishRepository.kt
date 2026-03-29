package com.teddy.mirandaytoledo.catalog.frames.finishes.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface FinishRepository {
    suspend fun getAll(): Result<List<Finish>, NetworkError>
    suspend fun create(name: String): Result<Finish, NetworkError>
    suspend fun update(id: Int, name: String, isActive: Boolean): Result<Finish, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
