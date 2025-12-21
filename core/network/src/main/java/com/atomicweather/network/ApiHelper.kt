package com.atomicweather.network

import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): AppResult<T> {
    return try {
        val result = apiCall()
        AppResult.Success(result)
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        AppResult.Error(
            AppError.Http(
                code = e.code(),
                message = e.message()
            ),
            e
        )
    } catch (e: SocketTimeoutException) {
        AppResult.Error(AppError.Timeout, e)
    } catch (e: UnknownHostException) {
        AppResult.Error(AppError.NoInternet, e)
    } catch (e: SerializationException) {
        AppResult.Error(AppError.Serialization, e)
    } catch (e: IOException) {
        AppResult.Error(AppError.Network, e)
    } catch (t: Throwable) {
        AppResult.Error(AppError.Unknown(t.message), t)
    }
}