package test.citron.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Everything related to spaces, paddings, margins and separations in the app. if you need more spaces, add here.
 */
@Immutable
class Spacing(
    val none: Dp = 0.dp,
    val xsmall: Dp = 5.dp,
    val small: Dp = 10.dp,
    val medium: Dp = 15.dp,
    val large: Dp = 20.dp,
    val xlarge: Dp = 30.dp,
    val xxlarge: Dp = 40.dp,
    val elevationMin: Dp = 4.dp,
    val elevationMax: Dp = 8.dp
)
