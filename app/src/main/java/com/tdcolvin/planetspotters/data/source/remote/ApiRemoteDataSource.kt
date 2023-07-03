package com.tdcolvin.planetspotters.data.source.remote

import android.os.SystemClock
import com.tdcolvin.planetspotters.data.repository.Planet
import kotlinx.coroutines.delay
import java.time.Clock
import java.util.UUID
import javax.inject.Inject

class ApiRemoteDataSource @Inject constructor(): RemoteDataSource {
    private val planetsCache = ArrayList<Planet>()
    private var lastDelay = 0L

    override suspend fun getPlanets(): List<Planet> {
        simulateApiDelay()
        return planetsCache
    }

    override suspend fun addPlanet(planet: Planet): Planet {
        simulateApiDelay()
        val planetToAdd = if (planet.planetId == null) planet.copy(planetId = UUID.randomUUID().toString()) else planet
        planetsCache.add(planetToAdd)
        return planetToAdd
    }

    override suspend fun deletePlanet(planetId: String) {
        simulateApiDelay()
        planetsCache.removeIf { it.planetId == planetId }
    }

    private suspend fun simulateApiDelay() {
        //(this logic is purely to avoid 3x simulated delays when adding the sample planets)
        if (SystemClock.uptimeMillis() > lastDelay + 500) {
            delay(2000)
            lastDelay = SystemClock.uptimeMillis()
        }
    }
}