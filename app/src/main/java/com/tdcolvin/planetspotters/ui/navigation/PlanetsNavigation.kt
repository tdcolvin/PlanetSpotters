package com.tdcolvin.planetspotters.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.tdcolvin.planetspotters.ui.navigation.PlanetsDestinationsArgs.PLANET_ID_ARG
import com.tdcolvin.planetspotters.ui.navigation.PlanetsDestinationsArgs.TITLE_ARG
import com.tdcolvin.planetspotters.ui.navigation.PlanetsDestinationsArgs.USER_MESSAGE_ARG
import com.tdcolvin.planetspotters.ui.navigation.PlanetsScreens.ADD_EDIT_PLANET_SCREEN
import com.tdcolvin.planetspotters.ui.navigation.PlanetsScreens.PLANETS_SCREEN

/**
 * Screens used in [PlanetsDestinations]
 */
private object PlanetsScreens {
    const val PLANETS_SCREEN = "planets"
    const val ADD_EDIT_PLANET_SCREEN = "addEditPlanet"
}

/**
 * Arguments used in [PlanetsDestinations] routes
 */
object PlanetsDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val PLANET_ID_ARG = "planetId"
    const val TITLE_ARG = "title"
}

/**
 * Destinations used in the [MainActivity]
 */
object PlanetsDestinations {
    const val PLANETS_ROUTE = "$PLANETS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val ADD_EDIT_PLANET_ROUTE = "$ADD_EDIT_PLANET_SCREEN/{$TITLE_ARG}?$PLANET_ID_ARG={$PLANET_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class PlanetsNavigationActions(private val navController: NavHostController) {

    fun navigateToPlanets(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            PLANETS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToAddEditPlanet(title: Int, planetId: String?) {
        navController.navigate(
            "$ADD_EDIT_PLANET_SCREEN/$title".let {
                if (planetId != null) "$it?$PLANET_ID_ARG=$planetId" else it
            }
        )
    }
}
