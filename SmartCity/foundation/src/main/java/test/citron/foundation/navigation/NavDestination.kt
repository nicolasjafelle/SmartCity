package test.citron.foundation.navigation

/**
 * Key interface to define Navigation Destinations and expose it to the root's navigation graph.
 * This is helpful to expose publicly the route and how to create it from the outside.
 *
 * Ideally to navigate between feature modules.
 */
interface NavDestination {

    /**
     * Defines the route for the destination.
     */
    val route: String

    /**
     * Creates the route for the destination with the given arguments or
     * null if the route is the same as [route]
     */
    fun createRoute(args: Map<String, Any?> = emptyMap()): String
}
