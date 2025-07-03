package test.citron.app

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import test.citron.design.theme.CitronTheme
import test.citron.maps.navigation.MapsNavBuilder
import test.citron.search.navigation.SearchNavBuilder

@Composable
fun CitronApp(deviceCapability: DeviceCapability) {
    CitronTheme {
        val navController = rememberNavController()

        when (deviceCapability) {
            DeviceCapability.FlatPortrait -> CitronPortraitNavHost(navController)
            DeviceCapability.FlatLandscape -> CitronLandscapeNavHost(navController)
            DeviceCapability.Book -> CitronBookModeNavHost(navController)
            DeviceCapability.Tabletop -> CitronTabletopNavHost(navController)
        }
    }
}

/**
 * NavHost for portrait devices
 */
@Composable
private fun CitronPortraitNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SearchNavBuilder.entryPoint) {
        SearchNavBuilder.attach(this, navController, onCitySelected = {
            navController.navigate(MapsNavBuilder.navigateToMap(it))
        })
        MapsNavBuilder.attach(this, navController)
    }
}

/**
 * NavHost for landscape devices
 */
@Composable
private fun CitronLandscapeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SearchNavBuilder.entryPoint) {
        SearchNavBuilder.attach(this, navController, onCitySelected = {
            navController.navigate(MapsNavBuilder.navigateToMap(it))
        })

        MapsNavBuilder.attach(this, navController)
    }
}

/**
 * NavHost for book mode devices in foldables.
 */
@Composable
private fun CitronBookModeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SearchNavBuilder.entryPoint) {
        SearchNavBuilder.attach(this, navController, onCitySelected = {
            navController.navigate(MapsNavBuilder.navigateToMap(it))
        })
        MapsNavBuilder.attach(this, navController)
    }
}

/**
 * NavHost for tabletop mode devices in foldables.
 */
@Composable
private fun CitronTabletopNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SearchNavBuilder.entryPoint) {
        SearchNavBuilder.attach(this, navController, onCitySelected = {
            navController.navigate(MapsNavBuilder.navigateToMap(it))
        })
        MapsNavBuilder.attach(this, navController)
    }
}

enum class DeviceCapability {
    FlatPortrait, // classic portrait
    FlatLandscape, // classic landscape
    Book, // fold horizontal + half-opened
    Tabletop // fold vertical + half-opened
}

/**
 * Returns the current device capability.
 */
@Composable
fun rememberScreenCapabilities(): DeviceCapability {
    val context = LocalContext.current
    val repo = remember { WindowInfoTracker.getOrCreate(context) }
    val layoutInfo by repo.windowLayoutInfo(context)
        .collectAsStateWithLifecycle(initialValue = null)

    val orientation = LocalConfiguration.current.orientation

    val foldingFeature =
        layoutInfo?.displayFeatures
            ?.filterIsInstance<FoldingFeature>()
            ?.firstOrNull()

    return when {
        foldingFeature == null -> {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                DeviceCapability.FlatLandscape
            } else {
                DeviceCapability.FlatPortrait
            }
        }

        foldingFeature.state == FoldingFeature.State.FLAT -> {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                DeviceCapability.FlatLandscape
            } else {
                DeviceCapability.FlatPortrait
            }
        }

        foldingFeature.state == FoldingFeature.State.HALF_OPENED -> {
            when (foldingFeature.orientation) {
                FoldingFeature.Orientation.HORIZONTAL -> DeviceCapability.Tabletop
                FoldingFeature.Orientation.VERTICAL -> DeviceCapability.Book
                else -> DeviceCapability.FlatPortrait
            }
        }

        else -> DeviceCapability.FlatPortrait
    }
}
