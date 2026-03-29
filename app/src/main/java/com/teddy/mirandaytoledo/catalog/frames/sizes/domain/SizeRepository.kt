package com.teddy.mirandaytoledo.catalog.frames.sizes.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface SizeRepository {
    suspend fun getAll(): Result<List<Size>, NetworkError>
    suspend fun create(name: String, sizeGroup: String): Result<Size, NetworkError>
    suspend fun update(
        id: Int,
        name: String,
        sizeGroup: String,
        isActive: Boolean
    ): Result<Size, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
