package test.citron.maps.view

import androidx.compose.runtime.Immutable
import test.citron.foundation.mvvm.SideEffect
import test.citron.foundation.mvvm.ViewState

@Immutable
data class MapsState(
    val mapLoaded: Boolean = true,
    val error: Error? = null,
    val uiObject: UiObject? = null
) : ViewState {
    companion object {
        val initial = MapsState()
    }
}

@Immutable
data class Error(
    val message: String
)

@Immutable
data class UiObject(
    val id: Long,
    val label: String,
    val isFavorite: Boolean,
    val latitude: Double,
    val longitude: Double
)

interface ViewModelEvent {
    fun onLoadCity(cityId: Long)
    fun onMapLoaded()
}

sealed class MapsScreenSideEffect : SideEffect {
    data class NavigateNext(val id: Long) : MapsScreenSideEffect()
    data class ShowError(val message: String) : MapsScreenSideEffect()
}
