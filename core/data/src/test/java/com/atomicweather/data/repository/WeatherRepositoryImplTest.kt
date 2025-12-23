package com.atomicweather.data.repository

import com.atomicaweather.domain.model.Forecast
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import com.atomicweather.data.remote.api.WeatherApi
import com.atomicweather.data.remote.dto.ForecastResponseDto
import com.atomicweather.data.remote.mapper.ForecastDtoToDomainMapper
import com.atomicweather.network.safeApiCall
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private val weatherApi: WeatherApi = mockk()
    private val mapper: ForecastDtoToDomainMapper = mockk()
    private val apiKey = "test_key"

    // Subject to test
    private val repo = WeatherRepositoryImpl(weatherApi, mapper, apiKey)
    val lat = 25.0
    val lon = 55.0

    @Before
    fun setup() {
        mockkStatic("com.atomicweather.network.ApiHelperKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("com.atomicweather.network.ApiHelperKt")
    }

    @Test
    fun `when api success and domain list not empty returns Success`() = runTest {
        // given
        val dto: ForecastResponseDto = mockk()
        val domain = Forecast(list = listOf(mockk()), city = null)
        coEvery { weatherApi.get5Day3HourForecast(lat, lon, apiKey) } returns dto

        // when
        coEvery { safeApiCall<ForecastResponseDto>(any()) } coAnswers {
            val block = firstArg<suspend () -> ForecastResponseDto>()
            AppResult.Success(block())
        }
        every { mapper.map(dto) } returns domain

        // then
        val result = repo.getWeatherForecast(lat, lon)
        assertEquals(AppResult.Success(domain), result)
        verify(exactly = 1) { mapper.map(dto) }
        coVerify(exactly = 1) { weatherApi.get5Day3HourForecast(lat, lon, apiKey) }
    }

    @Test
    fun `api success but empty domain returns EmptyData`() = runTest {
        // given
        val dto = ForecastResponseDto() // or mockk()
        coEvery { weatherApi.get5Day3HourForecast(lat, lon, apiKey) } returns dto

        // when
        coEvery {
            safeApiCall<ForecastResponseDto>(any())
        } coAnswers {
            val block = firstArg<suspend () -> ForecastResponseDto>()
            AppResult.Success(block())
        }
        every { mapper.map(dto) } returns Forecast(list = emptyList(), city = null)

        // then
        val result = repo.getWeatherForecast(lat, lon)
        assertEquals(AppResult.Error(AppError.EmptyData), result)
        coVerify(exactly = 1) { weatherApi.get5Day3HourForecast(lat, lon, apiKey, "metric") }
    }

    @Test
    fun `when api error returns same error and does not map`() = runTest {
        // given
        val apiError = AppResult.Error(AppError.Network)
        coEvery { weatherApi.get5Day3HourForecast(lat, lon, apiKey) } throws RuntimeException("network")

        // when
        coEvery { safeApiCall<ForecastResponseDto>(any()) } coAnswers {
            val block = firstArg<suspend () -> ForecastResponseDto>()
            try {
                block()
                apiError
            } catch (_: Throwable) {
                apiError
            }
        }

        // then
        val result = repo.getWeatherForecast(lat, lon)
        assertEquals(apiError, result)
        verify(exactly = 0) { mapper.map(any()) }
        coVerify(exactly = 1) { weatherApi.get5Day3HourForecast(lat, lon, apiKey) }
    }
}