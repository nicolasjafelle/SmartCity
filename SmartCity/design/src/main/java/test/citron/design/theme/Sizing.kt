package test.citron.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class Sizing(
    val none: Dp = 0.dp,
    val rowHeight: Dp = 50.dp,
    val favoriteIconSize: Dp = 40.dp,
    val roundedCornerMedium: Dp = 14.dp,
    val circularProgress: Dp = 50.dp
)
