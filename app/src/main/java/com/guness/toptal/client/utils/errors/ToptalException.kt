package com.guness.toptal.client.utils.errors

import com.guness.toptal.client.R
import com.guness.toptal.client.utils.SingleRun
import com.guness.toptal.client.utils.Title
import com.guness.toptal.protocol.dto.ErrorMessage
import retrofit2.HttpException
import java.io.IOException

sealed class ToptalException constructor(exception: Throwable, val singleRun: SingleRun = SingleRun()) : Exception(exception) {
    data class ApiError(val url: String, val status: Int, val response: ErrorMessage, val exception: HttpException) : ToptalException(exception)
    data class HttpError(val url: String, val status: Int, val exception: HttpException) : ToptalException(exception)
    data class NetworkError(val url: String, val exception: IOException) : ToptalException(exception)
    data class UnknownError(val exception: Throwable) : ToptalException(exception)
    data class LocalError(override val message: String) : ToptalException(Exception(message))

    companion object {
        fun from(err: Throwable) = err as? ToptalException ?: UnknownError(err)
    }
}

val ToptalException.fresh
    get() = !singleRun.isUsed

fun ToptalException.handle(action: () -> Unit) = singleRun.use(action)

val ToptalException.displayMessage
    get() = when (this) {
        is ToptalException.ApiError -> Title.Text(response.message)
        is ToptalException.HttpError -> getHttpErrorMessage(status)
        is ToptalException.NetworkError -> Title.Resource(R.string.connection_error)
        is ToptalException.UnknownError -> Title.Resource(R.string.unknown_error)
        is ToptalException.LocalError -> Title.Text(message)
    }

private fun getHttpErrorMessage(status: Int) = Title.Resource(
    when (status) {
        401 -> R.string.unauthorized_access
        409 -> R.string.duplicate_record
        422 -> R.string.cannot_process_request
        else -> R.string.protocol_error
    }
)