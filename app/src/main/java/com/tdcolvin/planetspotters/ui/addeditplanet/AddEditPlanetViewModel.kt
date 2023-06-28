
package com.tdcolvin.planetspotters.ui.addeditplanet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdcolvin.planetspotters.data.repository.Planet
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.repository.WorkResult
import com.tdcolvin.planetspotters.domain.AddPlanetUseCase
import com.tdcolvin.planetspotters.ui.navigation.PlanetsDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class AddEditPlanetUiState(
    val planetName: String = "",
    val planetDistanceLy: Float = 1.0F,
    val planetDiscovered: Date = Date(),
    val isLoading: Boolean = false,
    val isPlanetSaved: Boolean = false
)

@HiltViewModel
class AddEditPlanetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addPlanetUseCase: AddPlanetUseCase,
    private val planetsRepository: PlanetsRepository
): ViewModel() {
    private val planetId: String? = savedStateHandle[PlanetsDestinationsArgs.PLANET_ID_ARG]

    private val _uiState = MutableStateFlow(AddEditPlanetUiState())
    val uiState: StateFlow<AddEditPlanetUiState> = _uiState.asStateFlow()

    init {
        if (planetId != null) {
            loadPlanet(planetId)
        }
    }

    fun savePlanet() {
        viewModelScope.launch {
            addPlanetUseCase(
                Planet(
                    planetId = planetId,
                    name = _uiState.value.planetName,
                    distanceLy = uiState.value.planetDistanceLy,
                    discovered = uiState.value.planetDiscovered
                )
            )
            _uiState.update { it.copy(isPlanetSaved = true) }
        }
    }

    private fun loadPlanet(planetId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = planetsRepository.getPlanetFlow(planetId).first()
            if (result !is WorkResult.Success || result.data == null) {
                _uiState.update { it.copy(isLoading = false) }
            }
            else {
                val planet = result.data
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        planetName = planet.name,
                        planetDistanceLy = planet.distanceLy,
                        planetDiscovered = planet.discovered
                    )
                }
            }
        }
    }

    fun setPlanetName(name: String) {
        _uiState.update { it.copy(planetName = name) }
    }

    fun setPlanetDistanceLy(distanceLy: Float) {
        _uiState.update { it.copy(planetDistanceLy = distanceLy) }
    }
}