package test.citron.search.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import test.citron.design.AppBar
import test.citron.design.RowFavoriteItem
import test.citron.design.RowItem
import test.citron.design.SearchTextField
import test.citron.design.theme.CitronTheme
import test.citron.foundation.mvvm.observeWithLifecycle
import test.citron.search.R

@Composable
internal fun SearchScreen(viewModel: SearchViewModel, onCitySelected: (Long) -> Unit) {
    val modifier = Modifier
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffect.observeWithLifecycle { sideEffect ->
        when (sideEffect) {
            is SearchSideEffect.NavigateNext -> onCitySelected.invoke(sideEffect.id)
            is SearchSideEffect.ShowError -> snackbarHostState.showSnackbar(sideEffect.message)
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.onPrefetch()
    }

    Scaffold(
        modifier = modifier,
        topBar = { DrawAppBar() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        SearchScreenRootContent(
            modifier = modifier,
            innerPadding = innerPadding,
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SearchScreenRootContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    uiState: SearchState,
    viewModel: SearchViewModel
) {
    SearchScreenContent(
        modifier = modifier,
        innerPadding = innerPadding,
        queryText = uiState.currentText,
        isLoading = uiState.isLoading,
        resultList = uiState.results,
        onSearchQuery = { viewModel.onSearch(it) },
        onRowClicked = { viewModel.onResultClicked(it) },
        onFavoriteClicked = { viewModel.onFavoriteClicked(it) }
    )
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    queryText: String,
    isLoading: Boolean,
    resultList: List<SearchResult>?,
    onSearchQuery: (String) -> Unit,
    onRowClicked: (SearchResult) -> Unit,
    onFavoriteClicked: (SearchResult) -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = CitronTheme.spacing.medium)
            .imePadding()
    ) {
        SearchTextField(
            modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            currentText = queryText,
            placeHolderText = stringResource(R.string.search_input_hint),
            onValueChange = { onSearchQuery.invoke(it) },
            paddingValues = PaddingValues(CitronTheme.spacing.none),
            enabled = true,
            onFocusAction = {},
            textStyle = null
        )

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = rememberLazyListState()
        ) {
            resolveList(
                isloading = isLoading,
                results = resultList,
                onRowClicked = onRowClicked,
                onFavoriteClicked = onFavoriteClicked
            )
        }
    }
}

private fun LazyListScope.resolveList(
    isloading: Boolean,
    results: List<SearchResult>?,
    onRowClicked: (SearchResult) -> Unit,
    onFavoriteClicked: (SearchResult) -> Unit
) {
    when {
        isloading -> item { RowItem(text = stringResource(R.string.search_input_loading)) }

        results == null -> item { RowItem(text = stringResource(R.string.search_input_empty)) }

        results.isEmpty() ->
            item { RowItem(text = stringResource(R.string.search_input_no_results)) }

        else ->
            items(items = results) { item ->
                RowFavoriteItem(
                    text = item.fullName,
                    isFavorite = item.isFavorite,
                    onRowClicked = { onRowClicked.invoke(item) },
                    onFavoriteClicked = { onFavoriteClicked.invoke(item) }
                )
            }
    }
}

@Composable
private fun DrawAppBar() {
    AppBar(appBarTitle = stringResource(R.string.search_title))
}

@Preview
@Composable
private fun SearchScreenContentPreview() {
    val resultList =
        listOf(
            SearchResult(
                id = 1L,
                fullName = "New York",
                isFavorite = false,
                latitude = 40.7128,
                longitude = -74.0060
            ),
            SearchResult(
                id = 2L,
                fullName = "London",
                isFavorite = true,
                latitude = 51.5074,
                longitude = 0.1278
            ),
            SearchResult(
                id = 1L,
                fullName = "New Jersey",
                isFavorite = false,
                latitude = 40.7128,
                longitude = -74.0060
            ),
            SearchResult(
                id = 2L,
                fullName = "New Pepe",
                isFavorite = true,
                latitude = 51.5074,
                longitude = 0.1278
            )
        )
    SearchScreenContent(
        modifier = Modifier,
        innerPadding = PaddingValues(),
        queryText = "New",
        isLoading = false,
        resultList = resultList,
        onSearchQuery = {},
        onRowClicked = {},
        onFavoriteClicked = {}
    )
}

@Preview
@Composable
private fun DrawAppBarPreview() {
    DrawAppBar()
}
