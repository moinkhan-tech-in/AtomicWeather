package com.atomicweather.common.result

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AppError, val cause: Throwable? = null) : AppResult<Nothing>
}