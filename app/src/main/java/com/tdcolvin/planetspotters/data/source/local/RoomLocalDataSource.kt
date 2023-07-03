/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tdcolvin.planetspotters.data.source.local

import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.WorkResult
import com.tdcolvin.planetspotters.data.source.local.LocalDataSource
import com.tdcolvin.planetspotters.data.source.local.PlanetsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class RoomLocalDataSource internal constructor(
    private val planetsDao: PlanetsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {
    override fun getPlanetsFlow(): Flow<WorkResult<List<Planet>>> {
        return planetsDao.observePlanets().map {
            WorkResult.Success(it.map { planetEntity -> planetEntity.toPlanet() })
        }
    }

    override fun getPlanetFlow(planetId: String): Flow<WorkResult<Planet?>> {
        return planetsDao.observePlanetById(planetId).map {
            WorkResult.Success(it?.toPlanet())
        }
    }

    override suspend fun setPlanets(planets: List<Planet>) {
        planetsDao.setPlanets(planets.map { it.toPlanetEntity() })
    }

    override suspend fun addPlanet(planet: Planet) {
        planetsDao.insertPlanet(planet.toPlanetEntity())
    }

    override suspend fun deletePlanet(planetId: String) = withContext<Unit>(ioDispatcher) {
        planetsDao.deletePlanetById(planetId)
    }
}
