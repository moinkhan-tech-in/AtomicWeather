package com.atomicweather.common.result

sealed interface AppError {
    // network
    data object NoInternet : AppError
    data object Timeout : AppError
    data object Network : AppError

    // http
    data class Http(val code: Int, val message: String? = null) : AppError

    // data/parsing
    data object Serialization : AppError
    data object EmptyData : AppError

    // fallback
    data class Unknown(val message: String? = null) : AppError
}