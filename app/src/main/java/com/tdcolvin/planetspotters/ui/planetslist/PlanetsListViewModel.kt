package com.tdcolvin.planetspotters.ui.planetslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.repository.WorkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanetsListUiState(
    val planets: List<Planet> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@HiltViewModel
class PlanetsListViewModel @Inject constructor(
    private val planetsRepository: PlanetsRepository
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