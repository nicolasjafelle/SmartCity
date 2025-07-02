package test.citron.design

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import test.citron.design.theme.CitronTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    appBarTitle: String,
    onNavigationIconClick: () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = appBarTitle) },
        actions = actions,
        navigationIcon =
        navigationIcon?.let { navIcon ->
            {
                IconButton(onClick = onNavigationIconClick) {
                    navIcon.invoke()
                }
            }
        } ?: {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    CitronTheme {
        AppBar(
            appBarTitle = "Preview Title",
            navigationIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            },
            onNavigationIconClick = { }
        )
    }
}
