package com.tdcolvin.planetspotters.data.repository

interface PlanetsRepository {
    suspend fun addPlanet(planet: Planet)
}