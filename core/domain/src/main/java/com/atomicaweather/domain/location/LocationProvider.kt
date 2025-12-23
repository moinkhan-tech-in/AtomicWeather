package com.atomicaweather.domain.location

import com.atomicweather.common.model.LatLon
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getCurrentLatLon(): Flow<LatLon?>
}