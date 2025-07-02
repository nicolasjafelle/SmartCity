package test.citron.foundation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavigationBuilder {
    fun NavGraphBuilder.attachNavGraph(navController: NavHostController)
}
