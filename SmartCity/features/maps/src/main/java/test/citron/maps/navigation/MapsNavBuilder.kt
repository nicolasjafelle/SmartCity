package test.citron.maps.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import test.citron.foundation.navigation.NavDestination
import test.citron.foundation.navigation.NavigationBuilder
import test.citron.maps.navigation.MapsDestination.ARG_CITY_ID
import test.citron.maps.view.MapsScreen
import test.citron.maps.view.MapsViewModel

class MapsNavBuilder private constructor() : NavigationBuilder {

    override fun NavGraphBuilder.attachNavGraph(navController: NavHostController) {
        composable(MapsDestination.route) {
            val citySelectedId = it.arguments?.getString(ARG_CITY_ID)?.toLong() ?: 0
            val viewModel: MapsViewModel = hiltViewModel()
            MapsScreen(
                viewModel = viewModel,
                cityId = citySelectedId,
                onBack = { navController.popBackStack() }
            )
        }
    }

    companion object {
        fun navigateToMap(citySelectedId: Long) =
            MapsDestination.createRoute(mapOf(ARG_CITY_ID to citySelectedId))

        fun attach(builder: NavGraphBuilder, navController: NavHostController) {
            with(MapsNavBuilder()) {
                builder.attachNavGraph(navController)
            }
        }
    }
}

internal enum class Screen(val route: String) {
    MapsScreen("maps")
}

internal object MapsDestination : NavDestination {
    const val ARG_CITY_ID = "city_id"
    override val route = "${Screen.MapsScreen.route}/{${ARG_CITY_ID}}"

    override fun createRoute(args: Map<String, Any?>): String {
        var result = route
        args.forEach { (key, value) ->
            result = result.replace("{$key}", value.toString())
        }
        return result
    }
}
