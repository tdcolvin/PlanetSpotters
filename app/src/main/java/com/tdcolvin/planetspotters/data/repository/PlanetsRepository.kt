package com.tdcolvin.planetspotters.data.repository

import kotlinx.coroutines.flow.Flow

interface PlanetsRepository {
    fun getPlanets(): Flow<List<Planet>>
    suspend fun addPlanet(planet: Planet)
    suspend fun deletePlanet(planetId: String)
}