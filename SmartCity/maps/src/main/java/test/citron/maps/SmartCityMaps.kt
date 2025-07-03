package test.citron.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

/**
 * Composable which draw a map.
 *
 * @param modifier Modifier to apply to the layout.
 * @param latitude Latitude of the marker. Could be null in which case no marker is drawn.
 * @param longitude Longitude of the marker. Could be null in which case no marker is drawn.
 * @param zoomLevel Zoom level of the map.
 * @param onMapLoaded Callback to be invoked when the map is loaded.
 * @param content Content to be drawn on top of the map.
 */
@Composable
fun DrawMaps(
    modifier: Modifier,
    latitude: Double? = null,
    longitude: Double? = null,
    zoomLevel: Float = 15f,
    onMapLoaded: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (latitude == null || longitude == null) {
        Box(modifier = modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                onMapLoaded = onMapLoaded,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {}
            content.invoke()
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            val marker = LatLng(latitude, longitude)
            val markerState = rememberUpdatedMarkerState(position = marker)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(marker, zoomLevel)
            }

            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = onMapLoaded,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                Marker(state = markerState)
            }
            content.invoke()
        }
    }
}
