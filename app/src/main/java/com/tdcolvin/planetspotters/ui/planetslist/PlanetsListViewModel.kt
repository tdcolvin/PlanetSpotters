package com.tdcolvin.planetspotters.ui.planetslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.repository.WorkResult
import com.tdcolvin.planetspotters.domain.AddPlanetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class PlanetsListUiState(
    val planets: List<Planet> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@HiltViewModel
class PlanetsListViewModel @Inject constructor(
    private val addPlanetUseCase: AddPlanetUseCase,
    private val planetsRepository: PlanetsRepository
): ViewModel() {
    private val planets = planetsRepository.getPlanetsFlow()

    //How many things are we waiting for to load?
    private val numLoadingItems = MutableStateFlow(0)

    val uiState = combine(planets, numLoadingItems) { planets, loadingItems ->
        when (planets) {
            is WorkResult.Error -> PlanetsListUiState(isError = true)
            is WorkResult.Loading -> PlanetsListUiState(isLoading = true)
            is WorkResult.Success -> PlanetsListUiState(planets = planets.data, isLoading = loadingItems > 0)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlanetsListUiState(isLoading = true)
    )

    fun addSamplePlanets() {
        viewModelScope.launch {
            withLoading {
                val planets = arrayOf(
                    Planet(name = "Skaro", distanceLy = 0.5F, discovered = Date()),
                    Planet(name = "Trenzalore", distanceLy = 5F, discovered = Date()),
                    Planet(name = "Galifrey", distanceLy = 80F, discovered = Date()),
                )
                planets.forEach { addPlanetUseCase(it) }
            }
        }
    }

    fun deletePlanet(planetId: String) {
        viewModelScope.launch {
            withLoading {
                planetsRepository.deletePlanet(planetId)
            }
        }
    }

    fun refreshPlanetsList() {
        viewModelScope.launch {
            withLoading {
                planetsRepository.refreshPlanets()
            }
        }
    }

    private suspend fun withLoading(block: suspend () -> Unit) {
        try {
            addLoadingElement()
            block()
        }
        finally {
            removeLoadingElement()
        }
    }

    private fun addLoadingElement() = numLoadingItems.getAndUpdate { num -> num + 1 }
    private fun removeLoadingElement() = numLoadingItems.getAndUpdate { num -> num - 1 }
}