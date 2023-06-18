package com.tdcolvin.planetspotters.data.repository

import com.tdcolvin.planetspotters.data.source.LocalDataSource
import com.tdcolvin.planetspotters.data.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPlanetsRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val ioCoroutineDispatcher: CoroutineDispatcher
): PlanetsRepository {
    override fun getPlanetsFlow(): Flow<WorkResult<List<Planet>>> {
        return localDataSource.getPlanetsFlow()
    }

    override suspend fun refreshPlanets() {
        val planets = remoteDataSource.getPlanets()
        localDataSource.setPlanets(planets)
    }

    override suspend fun addPlanet(planet: Planet) {
        val planetWithId = remoteDataSource.addPlanet(planet)
        localDataSource.addPlanet(planetWithId)
    }

    override suspend fun deletePlanet(planetId: String) {
        remoteDataSource.deletePlanet(planetId)
        localDataSource.deletePlanet(planetId)
    }

}