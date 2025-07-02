package test.citron.foundation.mvvm

/**
 * Interface which represents a view state in a reactive view model approach.
 * UI state classes should typically be immutable data classes.
 */
interface ViewState

/**
 * interface which represents a one off single event in a reactive view model approach.
 * Side effect classes should typically be immutable data classes.
 *
 * **According to Android documentation**: A side-effect is a change to the state of the app that
 * happens outside the scope of a composable function. Due to composables' lifecycle and properties
 * such as unpredictable recompositions, executing recompositions of composables in different orders,
 * or recompositions that can be discarded, composables should ideally be side-effect free.
 *
 * However, sometimes side-effects are necessary, for example, to trigger a one-off event such as
 * showing a SnackBar or navigate to another screen given a certain state condition.
 * These actions should be called from a controlled environment that is aware of the lifecycle of the composable.
 */
interface SideEffect

/**
 * Interface defining the contract for dispatching UI state updates and
 * side effects.  This interface is typically implemented by a ViewModel
 * that manages state and side effects.
 *
 * @param UiState The type of the UI state.
 * @param UiEffect The type of the side effects.
 */
interface EventDispatcher<UiState : ViewState, UiEffect : SideEffect> {
    /**
     * Updates the UI state.
     *
     * @param state The new UI state.
     */
    fun updateState(block: (UiState) -> UiState)

    /**
     * Sends a side effect. Side Effects are one off events.
     *
     * @param sideEffect The side effect to send.
     */
    fun sendSideEffect(sideEffect: UiEffect)
}
