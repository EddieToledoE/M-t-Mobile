package com.teddy.mirandaytoledo.catalog.domain

import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result

interface EducationalLevelRepository {
    suspend fun getAll(): Result<List<EducationalLevel>, NetworkError>
    suspend fun create(name: String, maxGrade: Int): Result<EducationalLevel, NetworkError>
    suspend fun update(id: Int, name: String, maxGrade: Int, isActive: Boolean): Result<EducationalLevel, NetworkError>
    suspend fun delete(id: Int): EmptyResult<NetworkError>
}
