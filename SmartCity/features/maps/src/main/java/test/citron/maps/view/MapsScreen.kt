package test.citron.maps.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import test.citron.design.AppBar
import test.citron.design.theme.CitronTheme
import test.citron.design.theme.secondaryLight
import test.citron.foundation.mvvm.observeWithLifecycle
import test.citron.maps.DrawMaps
import test.citron.maps.R

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    viewModel: MapsViewModel,
    cityId: Long,
    onBack: () -> Unit
) {
    val modifier = Modifier
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffect.observeWithLifecycle { sideEffect ->
        when (sideEffect) {
            is MapsScreenSideEffect.NavigateNext -> snackbarHostState.showSnackbar(
                "not implemented"
            )
            is MapsScreenSideEffect.ShowError -> snackbarHostState.showSnackbar(sideEffect.message)
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        viewModel.onLoadCity(cityId)
    }

    Scaffold(
        modifier = modifier,
        topBar = { DrawAppBar(onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        MapsScreenRootContent(
            modifier = modifier,
            innerPadding = innerPadding,
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun DrawAppBar(onBack: () -> Unit) {
    AppBar(
        appBarTitle = stringResource(R.string.maps_title),
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        },
        onNavigationIconClick = { onBack.invoke() }
    )
}

@Composable
private fun MapsScreenRootContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    uiState: MapsState,
    viewModel: MapsViewModel
) {
    val uiObject = uiState.uiObject
    if (uiState.mapLoaded && uiObject != null) {
        DrawContentMap(
            modifier,
            innerPadding,
            uiObject.isFavorite,
            uiObject.label,
            uiObject.latitude,
            uiObject.longitude
        )
    } else {
        DrawContentLoading(modifier, innerPadding, onMapLoaded = { viewModel.onMapLoaded() })
    }
}

@Composable
private fun DrawContentMap(
    modifier: Modifier,
    innerPadding: PaddingValues,
    isFavorite: Boolean,
    label: String,
    latitude: Double,
    longitude: Double
) {
    DrawMaps(
        modifier = modifier.padding(innerPadding),
        latitude = latitude,
        longitude = longitude
    ) {
        Column(
            modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = 110.dp)
                .padding(horizontal = CitronTheme.spacing.medium),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = modifier
                    .fillMaxWidth()
                    .height(70.dp)

            ) {
                Row(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(CitronTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier.weight(1f),
                        text = label,
                        color = secondaryLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    val icon =
                        if (isFavorite) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        }

                    Image(
                        imageVector = icon,
                        contentDescription = "favorite"
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawContentLoading(
    modifier: Modifier,
    innerPadding: PaddingValues,
    onMapLoaded: () -> Unit
) {
    DrawMaps(
        modifier = modifier.padding(innerPadding),
        onMapLoaded = onMapLoaded
    ) {
        Column(
            modifier =
            modifier
                .padding(horizontal = CitronTheme.spacing.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(CitronTheme.sizing.circularProgress)
                    .align(Alignment.CenterHorizontally)

            )
        }
    }
}
