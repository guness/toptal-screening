package com.guness.toptal.client.utils.errors

import com.guness.toptal.protocol.dto.ErrorMessage
import retrofit2.HttpException
import java.io.IOException

sealed class ToptalException constructor(exception: Throwable) : Exception(exception) {
    data class ApiError(val url: String, val status: Int, val response: ErrorMessage, val exception: HttpException) : ToptalException(exception)
    data class HttpError(val url: String, val status: Int, val exception: HttpException) : ToptalException(exception)
    data class NetworkError(val url: String, val exception: IOException) : ToptalException(exception)
    data class UnknownError(val exception: Throwable) : ToptalException(exception)
    data class LocalError(override val message: String) : ToptalException(Exception(message))

    companion object {
        fun from(err: Throwable) = err as? ToptalException ?: UnknownError(err)
    }
}