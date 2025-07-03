package test.citron.foundation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Interface for defining and attaching a navigation graph to a [NavGraphBuilder].
 * Implementations of this interface should define the navigation destinations
 * (screens) and how they are connected using [NavGraphBuilder] and [NavHostController].
 */
interface NavigationBuilder {

    /**
     * Attaches the navigation graph to the given [NavGraphBuilder] using the provided [navController].
     *
     * This function should define all composable destinations and their corresponding navigation logic.
     * This is helpful to attach the navigation graph to the root's navigation graph.
     *
     * @param navController The [NavHostController] used to navigate between composables.
     */
    fun NavGraphBuilder.attachNavGraph(navController: NavHostController)
}
