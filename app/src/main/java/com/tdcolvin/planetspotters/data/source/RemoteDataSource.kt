package com.tdcolvin.planetspotters.data.source

import com.tdcolvin.planetspotters.data.repository.Planet

interface RemoteDataSource {
    suspend fun getPlanets(): List<Planet>
    suspend fun addPlanet(planet: Planet): Planet
    suspend fun deletePlanet(planetId: String)
}