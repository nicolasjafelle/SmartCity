package test.citron.foundation.mvvm

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Abstract base class for ViewModels that manage UI state and side effects.
 *
 * This class provides a foundation for ViewModels in a modern Android
 * architecture, using Kotlin Flows for state and side effect management.
 * It encourages a unidirectional data flow, where the ViewModel holds
 * the UI state and emits side effects that the UI can react to.
 *
 * This class uses [MutableStateFlow] for managing the UI state,
 * which provides efficient updates and easy observation from the UI.
 * It also uses a [Channel] to handle one-time side effects, ensuring
 * that they are delivered to the UI only once.
 *
 * Subclasses should typically:
 * - Define data classes for their UI state (`UiState`) and side effects (`UiEffect`).
 *   These classes should ideally be immutable.
 * - Initialize the [_uiState] with the initial state in the constructor.
 * - Implement methods to update the UI state based on user interactions
 *   or other events, using the [updateState] method.
 * - Trigger side effects using the [sendSideEffect] method.
 *
 * This class extends [BaseViewModel] (which is assumed to provide
 * common ViewModel functionality) and implements [EventDispatcher]
 * (which defines the contract for state and side effect management).
 *
 * @param UiState The type of the UI state, typically an immutable data class.
 * @param UiEffect The type of the side effects, typically an immutable data class.
 * @param initialState The initial UI state.
 *
 * @see kotlinx.coroutines.flow.MutableStateFlow
 * @see kotlinx.coroutines.flow.StateFlow
 * @see kotlinx.coroutines.channels.Channel
 * @see BaseViewModel
 * @see EventDispatcher
 */
@Suppress("PropertyName")
abstract class StateFullViewModel<UiState : ViewState, UiEffect : SideEffect>(
    initialState: UiState
) :
    ViewModel(), EventDispatcher<UiState, UiEffect> {
    /**
     * Mutable state flow holding the current UI state.  This is internal
     * to the ViewModel.
     */

    protected val _uiState = MutableStateFlow<UiState>(initialState)

    /**
     * Read-only state flow exposing the current UI state to the UI.
     */
    val uiState = _uiState.asStateFlow()

    /**
     * A private [Channel] used to emit one-time UI-related side effects from the ViewModel
     * to the Compose UI.
     *
     * This [Channel] is designed for events that should be consumed only once and do not represent
     * a continuous state (e.g., showing a Snackbar, navigating to a new screen, displaying a Toast).
     */
    private val _sideEffectChannel = Channel<UiEffect>(Channel.BUFFERED)

    /**
     * Flow of side effects. The UI can collect from this flow to react
     * to one-time events.
     */
    val sideEffect = _sideEffectChannel.receiveAsFlow()

    /**
     * Updates the UI state. This method should be used by subclasses to
     * modify the UI state and trigger recomposition in the UI.
     *
     * @param block A lambda function that takes the current [UiState] as its argument (`it`)
     *  * and should return the new, transformed [UiState]. This lambda defines the logic for
     *  * how the state should be changed based on its current value.
     */
    override fun updateState(block: (UiState) -> UiState) {
        _uiState.update {
            block.invoke(it)
        }
    }

    /**
     * Sends a side effect to the UI. This method should be used to
     * trigger one-time events or actions in the UI, such as showing
     * a SnackBar, navigating to another screen, etc.
     *
     * @param sideEffect The side effect to send.
     */
    override fun sendSideEffect(sideEffect: UiEffect) {
        viewModelScope.launch {
            withContext(Dispatchers.Main.immediate) {
                _sideEffectChannel.send(sideEffect)
            }
        }
    }
}

/**
 * Observes a [Flow] in a Compose [Composable] function, automatically collecting emissions
 * only when the [lifecycleOwner] is in at least the [minActiveState] (e.g., [Lifecycle.State.STARTED]).
 * This ensures that flow collection is tied to the Android lifecycle, preventing resource leaks
 * and unnecessary processing when the UI is not active.
 *
 * This inline function provides a convenient and safe way to collect flows from sources like
 * [androidx.lifecycle.ViewModel] into your composables.
 */
@Composable
inline fun <reified T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        withContext(Dispatchers.Main.immediate) {
            flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
        }
    }
}
