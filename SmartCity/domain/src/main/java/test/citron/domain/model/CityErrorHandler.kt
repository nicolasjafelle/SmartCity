package test.citron.domain.model

import java.io.IOException
import java.net.UnknownHostException
import test.citron.foundation.result.Result

object CityErrorHandler {
    fun <T> handleError(ex: Exception): Result<T, CityError> = when {
        ex is IOException && ex::class.simpleName == "HttpRequestTimeoutException" -> Result.Error(
            CityError.ConnectionError
        )
        ex is UnknownHostException -> Result.Error(CityError.ConnectionError)
        else -> Result.Error(CityError.Unknown)
    }

    fun <T> handleError(ex: Throwable): Result<T, CityError> = when {
        ex is IOException && ex::class.simpleName == "HttpRequestTimeoutException" -> Result.Error(
            CityError.ConnectionError
        )
        ex is UnknownHostException -> Result.Error(CityError.ConnectionError)
        else -> Result.Error(CityError.Unknown)
    }
}

enum class CityError {
    Unknown,
    NotFound,
    CityNotFound,
    ConnectionError
}
