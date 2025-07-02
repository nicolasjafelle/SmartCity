package test.citron.foundation.navigation

interface NavDestination {
    val route: String
    fun createRoute(args: Map<String, Any?> = emptyMap()): String
}
