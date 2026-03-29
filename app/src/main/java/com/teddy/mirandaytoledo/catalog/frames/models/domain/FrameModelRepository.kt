package com.teddy.mirandaytoledo.catalog.frames.models.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface FrameModelRepository {
    suspend fun getAll(): Result<List<FrameModel>, NetworkError>
    suspend fun create(name: String): Result<FrameModel, NetworkError>
    suspend fun update(id: Int, name: String): Result<FrameModel, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
