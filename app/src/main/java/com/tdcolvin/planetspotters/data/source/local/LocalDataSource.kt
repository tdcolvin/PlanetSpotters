package com.tdcolvin.planetspotters.data.source.local

import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.WorkResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getPlanetsFlow(): Flow<WorkResult<List<Planet>>>
    fun getPlanetFlow(planetId: String): Flow<WorkResult<Planet?>>
    suspend fun setPlanets(planets: List<Planet>)
    suspend fun addPlanet(planet: Planet)
    suspend fun deletePlanet(planetId: String)
}