package com.teddy.mirandaytoledo.core.data.networking

import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(error = NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(error = NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(error = NetworkError.UNKNOWN)
    }
return responseToResult(response)
}