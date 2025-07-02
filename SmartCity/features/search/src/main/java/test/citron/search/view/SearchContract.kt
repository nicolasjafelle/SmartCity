package test.citron.search.view

import androidx.compose.runtime.Immutable
import test.citron.foundation.mvvm.SideEffect
import test.citron.foundation.mvvm.ViewState

data class SearchState(
    val isLoading: Boolean = false,
    val isPrefetching: Boolean = false,
    val currentText: String = "",
    val error: Error? = null,
    val results: List<SearchResult>? = null
) : ViewState {
    companion object {
        val initial = SearchState()
    }
}

interface ViewModelEvent {
    fun onPrefetch()

    fun onSearch(query: String)

    fun onResultClicked(result: SearchResult)

    fun onFavoriteClicked(result: SearchResult)
}

@Immutable
data class SearchResult(
    val id: Long,
    val fullName: String,
    val isFavorite: Boolean,
    val latitude: Double,
    val longitude: Double
)

@Immutable
data class Error(
    val message: String
)

sealed interface SearchSideEffect : SideEffect {
    data class NavigateNext(val id: Long) : SearchSideEffect

    data class ShowError(val message: String) : SearchSideEffect
}
