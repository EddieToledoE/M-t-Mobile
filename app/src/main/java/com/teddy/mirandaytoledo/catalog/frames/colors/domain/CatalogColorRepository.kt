package com.teddy.mirandaytoledo.catalog.frames.colors.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface CatalogColorRepository {
    suspend fun getAll(): Result<List<CatalogColor>, NetworkError>
    suspend fun create(name: String, hex: String?): Result<CatalogColor, NetworkError>
    suspend fun update(id: Int, name: String, hex: String?): Result<CatalogColor, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
