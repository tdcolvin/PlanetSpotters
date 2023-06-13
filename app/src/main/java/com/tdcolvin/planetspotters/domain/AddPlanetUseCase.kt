package com.tdcolvin.planetspotters.domain

import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import java.util.Date
import javax.inject.Inject

class AddPlanetUseCase @Inject constructor(private val planetsRepository: PlanetsRepository) {
    suspend operator fun invoke(planet: Planet) {
        if (planet.name.isEmpty()) {
            throw Exception("Please specify a planet name")
        }
        if (planet.distanceLy < 0) {
            throw Exception("Please enter a positive distance")
        }
        if (planet.discovered.after(Date())) {
            throw Exception("Please enter a discovery date in the past")
        }
        planetsRepository.addPlanet(planet)
    }
}