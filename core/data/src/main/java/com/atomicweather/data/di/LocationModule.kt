package com.atomicweather.data.di

import com.atomicaweather.domain.location.LocationProvider
import com.atomicweather.data.location.FusedLocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindLocationProvider(impl: FusedLocationProvider): LocationProvider
}