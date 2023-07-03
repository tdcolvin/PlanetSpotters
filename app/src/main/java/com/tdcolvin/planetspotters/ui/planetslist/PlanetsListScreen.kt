package com.tdcolvin.planetspotters.ui.planetslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tdcolvin.planetspotters.R
import com.tdcolvin.planetspotters.data.repository.Planet
import java.util.Date

@Composable
fun PlanetsListScreen(
    addPlanet: () -> Unit,
    editPlanet: (String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: PlanetsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = addPlanet) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_planet))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = viewModel::refreshPlanetsList) {
                    Text("Refresh")
                }
                Button(onClick = viewModel::addSamplePlanets) {
                    Text("Add sample planets")
                }
            }

            if (uiState.planets.isEmpty()) {
                NoPlanetsInfo()
            }
            else {
                PlanetsList(
                    editPlanet = editPlanet,
                    deletePlanet = viewModel::deletePlanet,
                    planets = uiState.planets
                )
            }
        }

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Center))
            }
        }
    }
}

@Composable
fun NoPlanetsInfo() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
        Text(stringResource(R.string.no_planets_label), color = Color.Gray)
    }
}

@Composable
fun PlanetsList(
    editPlanet: (String) -> Unit,
    deletePlanet: (String) -> Unit,
    planets: List<Planet>
) {
    LazyColumn {
        items(planets) {
            PlanetItem(
                planet = it,
                onEditPlanet = editPlanet,
                onDeletePlanet = deletePlanet
            )
        }
    }
}

@Composable
fun PlanetItem(
    planet: Planet,
    onEditPlanet: (String) -> Unit,
    onDeletePlanet: (String) -> Unit
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .clickable { planet.planetId?.let { onEditPlanet(it) } }
        //.border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(10.dp))
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .background(Color(0xFFBB86FC))
        .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = planet.name,
                style = MaterialTheme.typography.h4,
            )

            Text("${planet.distanceLy} light years away")
        }

        IconButton(
            modifier = Modifier
                .align(CenterVertically)
                .width(32.dp)
                .height(32.dp),
            onClick = { planet.planetId?.let { onDeletePlanet(it) } }
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview
@Composable
fun PlanetPreview() {
    PlanetItem(
        Planet("", "Hey! owei fjqowfi qoefiqoewf hqeifuhqeiofuqhfuhew fiquefhqirfhqelfjalkhaerlughq;eoifwe;ofiwo;efije;afoiajergoi jerlogi eqroi goeir g", 0.5F, Date()),
        { }) { }
}