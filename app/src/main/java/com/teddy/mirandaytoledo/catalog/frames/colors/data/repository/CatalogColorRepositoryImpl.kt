package com.teddy.mirandaytoledo.catalog.frames.colors.data.repository

import com.teddy.mirandaytoledo.catalog.frames.colors.data.dto.CreateCatalogColorRequest
import com.teddy.mirandaytoledo.catalog.frames.colors.data.dto.UpdateCatalogColorRequest
import com.teddy.mirandaytoledo.catalog.frames.colors.data.mappers.toDomain
import com.teddy.mirandaytoledo.catalog.frames.colors.data.networking.CatalogColorRemoteDataSource
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColor
import com.teddy.mirandaytoledo.catalog.frames.colors.domain.CatalogColorRepository
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.core.domain.util.map

class CatalogColorRepositoryImpl(
    private val remoteDataSource: CatalogColorRemoteDataSource
) : CatalogColorRepository {

    override suspend fun getAll(): Result<List<CatalogColor>, NetworkError> {
        return remoteDataSource.getAll().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun create(name: String, hex: String?): Result<CatalogColor, NetworkError> {
        return remoteDataSource.create(
            CreateCatalogColorRequest(
                name = name,
                hex = hex
            )
        ).map { it.toDomain() }
    }

    override suspend fun update(id: Int, name: String, hex: String?): Result<CatalogColor, NetworkError> {
        return remoteDataSource.update(
            id = id,
            request = UpdateCatalogColorRequest(
                name = name,
                hex = hex
            )
        ).map { it.toDomain() }
    }

    override suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return remoteDataSource.delete(id = id)
    }
}
