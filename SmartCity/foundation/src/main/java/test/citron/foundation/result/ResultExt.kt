package test.citron.foundation.result

suspend fun <S, F> Result<S, F>.onSuccess(action: suspend (S) -> Unit): Result<S, F> {
    if (this is Result.Success) action(data)
    return this
}

suspend fun <S, F> Result<S, F>.onError(action: suspend (F) -> Unit): Result<S, F> {
    if (this is Result.Error) action(error)
    return this
}

fun <S, F> Result<S, F>.getOrNull(): S? = (this as? Result.Success)?.data

fun <S, F> Result<S, F>.getErrorOrNull(): F? = (this as? Result.Error)?.error
