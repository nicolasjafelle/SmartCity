package test.citron.maps.view

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import test.citron.domain.usecase.GetCityUseCase
import test.citron.foundation.mvvm.StateFullViewModel
import test.citron.foundation.result.Result

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapper: MapsMapper,
    private val getCitiesUseCase: GetCityUseCase
) :
    StateFullViewModel<MapsState, MapsScreenSideEffect>(MapsState.initial), ViewModelEvent {

    override fun onLoadCity(cityId: Long) {
        updateState { it.copy(mapLoaded = false) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getCitiesUseCase.invoke(cityId)
            }.also {
                when (it) {
                    is Result.Success -> {
                        updateState { state ->
                            state.copy(
                                uiObject = mapper.toUiObject(it.data),
                                mapLoaded = !state.mapLoaded
                            )
                        }
                    }

                    is Result.Error -> {
                        updateState { state ->
                            state.copy(
                                error = Error(it.error.name)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onMapLoaded() {
        updateState { state ->
            state.copy(mapLoaded = true)
        }
    }
}
