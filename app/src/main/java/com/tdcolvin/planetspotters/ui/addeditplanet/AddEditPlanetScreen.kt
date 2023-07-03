package com.tdcolvin.planetspotters.ui.addeditplanet

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tdcolvin.planetspotters.R

@Composable
fun AddEditPlanetScreen(
    onPlanetUpdate: () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: AddEditPlanetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (!uiState.isPlanetSaving) viewModel.savePlanet() }
            ) {
                Icon(Icons.Filled.Done, stringResource(R.string.save_planet_description))
            }
        }
    ) { paddingValues ->
        AddEditPlanetContent(
            loading = uiState.isLoading,
            saving = uiState.isPlanetSaving,
            name = uiState.planetName,
            distanceLy = uiState.planetDistanceLy,
            onNameChanged = { newName -> viewModel.setPlanetName(newName) },
            onDistanceLyChanged = { newDistanceLy -> viewModel.setPlanetDistanceLy(newDistanceLy) },
            modifier = Modifier.padding(paddingValues)
        )

        // Check if the planet is saved and call onPlanetUpdate event
        LaunchedEffect(uiState.isPlanetSaved) {
            if (uiState.isPlanetSaved) {
                onPlanetUpdate()
            }
        }
    }

    //Show the error message as a toast if we've just had an error
    val context = LocalContext.current
    val errorText = uiState.planetSavingError?.let { stringResource(it) }
    LaunchedEffect(errorText) {
        if (errorText != null) {
            Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun AddEditPlanetContent(
    loading: Boolean,
    saving: Boolean,
    name: String,
    distanceLy: Float,
    onNameChanged: (String) -> Unit,
    onDistanceLyChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    if (loading) {
        LoadingContent()
    }
    else {
        Column(
            modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.high)
            )
            OutlinedTextField(
                value = name,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onNameChanged,
                label = { Text(stringResource(R.string.plane_name_label)) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.name_hint),
                        style = MaterialTheme.typography.h6
                    )
                },
                textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = distanceLy.toString(),
                onValueChange = { try { it.toFloat().run { onDistanceLyChanged(this) }  } catch (_: NumberFormatException) { } },
                label = { Text(stringResource(R.string.distance_label)) },
                placeholder = { Text(stringResource(R.string.distance_description)) },
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
        }

        if (saving) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.DarkGray)
    }
}

@Preview
@Composable
fun AddEditContentPreview() {
    AddEditPlanetContent(
        saving = true,
        loading = true,
        name = "aaa",
        distanceLy = 1.0f,
        onNameChanged = {},
        onDistanceLyChanged = {}
    )
}