package com.atomicweather.feature.weather.main

import app.cash.turbine.test
import com.atomicaweather.domain.location.LocationProvider
import com.atomicaweather.domain.model.City
import com.atomicaweather.domain.model.Forecast
import com.atomicaweather.domain.model.ForecastItem
import com.atomicaweather.domain.usecase.GetWeatherForecastUseCase
import com.atomicweather.common.dispatcher.DispatcherProvider
import com.atomicweather.common.model.LatLon
import com.atomicweather.common.result.AppError
import com.atomicweather.common.result.AppResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherMainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dispatcherProvider = object : DispatcherProvider {
        override val io = mainDispatcherRule.dispatcher
        override val main = mainDispatcherRule.dispatcher
        override val default = mainDispatcherRule.dispatcher
    }

    private val getWeatherForecastUseCase: GetWeatherForecastUseCase = mockk()
    private val locationProvider: LocationProvider = mockk()

    private val lat = 25.2048
    private val lon = 55.2708
    private val fakeForecast = Forecast(
        city = City(name = "Dubai", timezone = 4 * 3600),
        list = listOf(ForecastItem())
    )

    @Test
    fun `init - when location available - calls usecase with lat lon`() = runTest {
        // given
        every { locationProvider.getCurrentLatLon() } returns flowOf(LatLon(lat, lon))
        every { getWeatherForecastUseCase(lat, lon) } returns flowOf(AppResult.Success(fakeForecast))

        // when
        val vm = WeatherMainViewModel(getWeatherForecastUseCase, locationProvider, dispatcherProvider)
        advanceUntilIdle()

        // then
        verify(exactly = 1) { locationProvider.getCurrentLatLon() }
        verify(exactly = 1) { getWeatherForecastUseCase(lat, lon) }
    }

    @Test
    fun `init - success flow - emits Loading then Success`() = runTest {

        // given
        every { locationProvider.getCurrentLatLon() } returns flow {
            kotlinx.coroutines.yield()
            emit(LatLon(lat, lon))
        }
        every { getWeatherForecastUseCase(lat, lon) } returns
                flowOf(AppResult.Success(fakeForecast))

        // when
        val vm = WeatherMainViewModel(getWeatherForecastUseCase, locationProvider, dispatcherProvider)

        // then
        vm.uiState.test {
            assertTrue(awaitItem() is WeatherMainUiState.Ideal)
            assertTrue(awaitItem() is WeatherMainUiState.Loading)
            assertTrue(awaitItem() is WeatherMainUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init - error flow - emits Loading then Error`() = runTest {
        // given
        every { locationProvider.getCurrentLatLon() } returns flow {
            kotlinx.coroutines.yield()
            emit(LatLon(lat, lon))
        }
        every { getWeatherForecastUseCase(lat, lon) } returns
                flowOf(AppResult.Error(AppError.Network))

        // when
        val vm = WeatherMainViewModel(getWeatherForecastUseCase, locationProvider, dispatcherProvider)

        // then
        vm.uiState.test {
            assertTrue(awaitItem() is WeatherMainUiState.Ideal)
            assertTrue(awaitItem() is WeatherMainUiState.Loading)
            assertTrue(awaitItem() is WeatherMainUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onEvent OnRetry - triggers location fetch again`() = runTest {
        // given
        every { locationProvider.getCurrentLatLon() } returns
                flowOf(LatLon(lat, lon))
        every { getWeatherForecastUseCase(lat, lon) } returns
                flowOf(AppResult.Success(fakeForecast))

        val vm = WeatherMainViewModel(getWeatherForecastUseCase, locationProvider, dispatcherProvider)
        advanceUntilIdle()

        // when
        vm.onEvent(WeatherMainUiEvent.OnRetry)
        advanceUntilIdle()

        // then
        verify(exactly = 2) { locationProvider.getCurrentLatLon() }
        verify(exactly = 2) { getWeatherForecastUseCase(lat, lon) }
    }

    @Test
    fun `onEvent OnPermissionGranted - triggers location fetch again`() = runTest {
        // given
        every { locationProvider.getCurrentLatLon() } returns flowOf(LatLon(lat, lon))
        every { getWeatherForecastUseCase(lat, lon) } returns
                flowOf(AppResult.Success(fakeForecast))

        val vm = WeatherMainViewModel(getWeatherForecastUseCase, locationProvider, dispatcherProvider)
        advanceUntilIdle()

        // when
        vm.onEvent(WeatherMainUiEvent.OnPermissionGranted)
        advanceUntilIdle()

        // then
        verify(exactly = 2) { locationProvider.getCurrentLatLon() }
        verify(exactly = 2) { getWeatherForecastUseCase(lat, lon) }
    }
}

/**
 * JUnit4 rule that sets Dispatchers.Main to a TestDispatcher.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : org.junit.rules.TestWatcher() {

    override fun starting(description: org.junit.runner.Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: org.junit.runner.Description) {
        Dispatchers.resetMain()
    }
}