package com.tdcolvin.planetspotters.data.repository

import kotlinx.coroutines.flow.Flow

interface PlanetsRepository {
    fun getPlanetsFlow(): Flow<WorkResult<List<Planet>>>
    fun getPlanetFlow(planetId: String): Flow<WorkResult<Planet?>>
    suspend fun refreshPlanets()
    suspend fun addPlanet(planet: Planet)
    suspend fun deletePlanet(planetId: String)
}