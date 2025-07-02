package test.citron.search.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import test.citron.foundation.navigation.NavigationBuilder
import test.citron.search.view.SearchScreen

class SearchNavBuilder private constructor(
    private val onCitySelected: (Long) -> Unit
) : NavigationBuilder {
    private enum class Screen(val route: String) {
        SearchScreen("search")
    }

    override fun NavGraphBuilder.attachNavGraph(navController: NavHostController) {
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(hiltViewModel(), onCitySelected = onCitySelected)
        }
    }

    companion object {
        val entryPoint = Screen.SearchScreen.route

        fun attach(
            builder: NavGraphBuilder,
            navController: NavHostController,
            onCitySelected: (Long) -> Unit
        ) {
            with(SearchNavBuilder(onCitySelected)) {
                builder.attachNavGraph(navController)
            }
        }
    }
}
