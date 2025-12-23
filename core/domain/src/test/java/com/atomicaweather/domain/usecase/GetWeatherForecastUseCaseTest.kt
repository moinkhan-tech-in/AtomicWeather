package com.atomicaweather.domain.usecase

import com.atomicaweather.domain.model.Forecast
import com.atomicaweather.domain.repository.WeatherRepository
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherForecastUseCaseTest {

    private val repository: WeatherRepository = mockk()
    val lat = 25.2048
    val lon = 55.2708

    // Subject to test
    private val useCase = GetWeatherForecastUseCase(repository)

    @Test
    fun `invoke emits Success from repository`() = runTest {
        // given
        val forecast: Forecast = mockk()
        val expected = AppResult.Success(forecast)
        coEvery { repository.getWeatherForecast(lat, lon) } returns expected

        // when
        val actual = useCase(lat, lon).first()

        // then
        assertEquals(expected, actual)
        coVerify(exactly = 1) { repository.getWeatherForecast(lat, lon) }
    }

    @Test
    fun `invoke emits Error from repository`() = runTest {
        // given
        val expected = AppResult.Error(AppError.Network)
        coEvery { repository.getWeatherForecast(lat, lon) } returns expected

        // when
        val actual = useCase(lat, lon).first()

        // then
        assertEquals(expected, actual)
        coVerify(exactly = 1) { repository.getWeatherForecast(lat, lon) }
    }
}