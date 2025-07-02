package test.citron.search.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import test.citron.domain.model.CityError
import test.citron.domain.usecase.AddFavoriteUseCase
import test.citron.domain.usecase.FetchCityListUseCase
import test.citron.domain.usecase.RemoveFavoriteUseCase
import test.citron.domain.usecase.SearchCityUseCase
import test.citron.foundation.mvvm.StateFullViewModel
import test.citron.foundation.result.onError
import test.citron.foundation.result.onSuccess
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val fetchCityListUseCase: FetchCityListUseCase,
    private val searchCityUseCase: SearchCityUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val mapper: SearchMapper
) : ViewModelEvent,
    StateFullViewModel<SearchState, SearchSideEffect>(SearchState.initial) {

    private val debounceTimeout = 400L
    private var job: Job? = null

    private val prefetchStatus = MutableStateFlow(false)

    override fun onPrefetch() {
        prefetchStatus
            .onEach { prefetched ->
                updateState { it.copy(isPrefetching = prefetched) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            fetchCityListUseCase.invoke()
                .onSuccess { result ->
                    prefetchStatus.value = true
                    Log.i("SearchViewModel", "Prefetch city result = $result")
                }.onError { error ->
                    prefetchStatus.value = true
                    Log.w("SearchViewModel", "Prefetch city error = $error")
                }
        }
    }

    override fun onSearch(query: String) {
        updateState { it.copy(currentText = query) }

        job?.cancel(cause = null)
        job = null

        if (query.isBlank()) {
            updateState { it.copy(results = null, isLoading = false) }
        }

        if (query.length <= 2) {
            updateState { it.copy(results = null, isLoading = false) }
            return
        }

        updateState {
            it.copy(
                currentText = query,
                isLoading = true
            )
        }

        job = viewModelScope.launch {
            prefetchStatus
                .filter { prefetched -> prefetched }
                .collect {
                    delay(debounceTimeout)

                    searchCityUseCase.invoke(query)
                        .flowOn(Dispatchers.IO)
                        .collect { result ->
                            result.onSuccess { list ->
                                updateState {
                                    it.copy(
                                        results = mapper.mapList(list),
                                        isLoading = false
                                    )
                                }
                            }.onError { error ->
                                onError(error)
                            }
                        }
                }
        }
    }

    override fun onResultClicked(result: SearchResult) {
        sendSideEffect(SearchSideEffect.NavigateNext(result.id))
    }

    override fun onFavoriteClicked(result: SearchResult) {
        viewModelScope.launch {
            if (result.isFavorite) {
                removeFavorite(result)
            } else {
                addFavorite(result)
            }
        }
    }

    private fun onError(error: CityError) {
        sendSideEffect(SearchSideEffect.ShowError(mapper.resolveError(error).message))
        updateState {
            it.copy(
                error = mapper.resolveError(error),
                isLoading = false
            )
        }
    }

    private suspend fun addFavorite(result: SearchResult) {
        addFavoriteUseCase.invoke(result.id)
            .onSuccess {
                updateState {
                    it.copy(
                        results =
                            it.results?.map { stored ->
                                if (stored.id == result.id) {
                                    stored.copy(isFavorite = true)
                                } else {
                                    stored
                                }
                            }
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        error = mapper.resolveError(error),
                        isLoading = false
                    )
                }
            }
    }

    private suspend fun removeFavorite(result: SearchResult) {
        removeFavoriteUseCase.invoke(result.id)
            .onSuccess {
                updateState {
                    it.copy(
                        results = it.results?.map { stored ->
                            if (stored.id == result.id) {
                                stored.copy(isFavorite = false)
                            } else {
                                stored
                            }
                        }
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        error = mapper.resolveError(error),
                        isLoading = false
                    )
                }
            }
    }
}
