package com.atomicweather.data.di

import com.atomicweather.data.remote.mapper.ForecastDtoToDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideForecastDtoToDomainMapper(): ForecastDtoToDomainMapper =
        ForecastDtoToDomainMapper()
}