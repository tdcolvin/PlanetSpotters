package com.tdcolvin.planetspotters.ui.planetslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.repository.WorkResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PlanetsListUiState(
    val planets: List<Planet> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class PlanetsListViewModel @Inject constructor(
    planetsRepository: PlanetsRepository
): ViewModel() {
    private val planets = planetsRepository.getPlanetsFlow()

    val uiState = planets.map { planets ->
        when (planets) {
            is WorkResult.Error -> PlanetsListUiState(isError = true)
            is WorkResult.Loading -> PlanetsListUiState(isLoading = true)
            is WorkResult.Success -> PlanetsListUiState(planets = planets.data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlanetsListUiState(isLoading = true)
    )
}