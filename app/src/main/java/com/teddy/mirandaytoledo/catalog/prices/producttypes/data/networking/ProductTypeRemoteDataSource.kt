package com.teddy.mirandaytoledo.catalog.prices.producttypes.data.networking

import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.CreateProductTypeRequest
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.ProductTypeDto
import com.teddy.mirandaytoledo.catalog.prices.producttypes.data.dto.UpdateProductTypeRequest
import com.teddy.mirandaytoledo.core.data.networking.constructUrl
import com.teddy.mirandaytoledo.core.data.networking.safeCall
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class ProductTypeRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun getAll(): Result<List<ProductTypeDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("/api/catalog/product-types")
            )
        }
    }

    suspend fun create(request: CreateProductTypeRequest): Result<ProductTypeDto, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("/api/catalog/product-types")
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun update(id: Int, request: UpdateProductTypeRequest): Result<ProductTypeDto, NetworkError> {
        return safeCall {
            httpClient.put(
                urlString = constructUrl("/api/catalog/product-types/$id")
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun delete(id: Int): EmptyResult<NetworkError> {
        return try {
            val response = httpClient.delete(
                urlString = constructUrl("/api/catalog/product-types/$id")
            )
            if (response.status.value in 200..299) {
                Result.Success(Unit)
            } else {
                when (response.status.value) {
                    401 -> Result.Error(NetworkError.INCORRECT_CREDENTIALS)
                    408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                    429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                    in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
                    else -> Result.Error(NetworkError.UNKNOWN)
                }
            }
        } catch (e: UnresolvedAddressException) {
            Result.Error(NetworkError.NO_INTERNET)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}
