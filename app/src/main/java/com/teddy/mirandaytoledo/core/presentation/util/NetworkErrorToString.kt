package com.teddy.mirandaytoledo.core.presentation.util

import android.content.Context
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.core.domain.util.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_many_request
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_server_error
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.INCORRECT_CREDENTIALS -> R.string.error_incorrect_credential
    }
    return context.getString(resId)
}