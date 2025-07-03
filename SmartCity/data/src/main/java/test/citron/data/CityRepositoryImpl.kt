package test.citron.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import test.citron.data.api.CityApiClient
import test.citron.data.entity.CityResponse
import test.citron.data.local.CityLocalStorage
import test.citron.data.local.FavoriteLocalStorage
import test.citron.domain.model.City
import test.citron.domain.model.Coordinate
import test.citron.domain.repository.CityRepository

/**
 * Implementation of [CityRepository] that provides city-related data operations.
 *
 * Combines a remote API ([CityApiClient]) and a local cache ([CityLocalStorage]),
 * with support for marking favorite cities ([FavoriteLocalStorage]).
 *
 * This class supports offline-first behavior: data is fetched from cache when available,
 * otherwise retrieved from the API and stored locally.
 */
internal class CityRepositoryImpl(
    private val apiClient: CityApiClient,
    private val localDataSource: CityLocalStorage,
    private val favoriteLocalStorage: FavoriteLocalStorage
) : CityRepository {

    /**
     * Fetches the city list from local storage if available, otherwise from the API.
     * The fetched data is cached locally.
     */
    override suspend fun fetchCityList(): Boolean {
        var storedList = localDataSource.get()

        if (storedList == null) {
            apiClient.getCityList().map {
                it.toDomain()
            }.also {
                storedList = it
                localDataSource.store(it)
            }
        }
        return storedList.isNullOrEmpty().not()
    }

    /**
     * Emits a flow with city data: first from local cache, then refreshed from the API.
     * @return [Flow] list of [City] objects.
     */
    override suspend fun getCityList(): Flow<List<City>> = flow {
        emit(localDataSource.get() ?: emptyList())

        apiClient.getCityList().map {
            it.toDomain()
        }.also {
            localDataSource.store(it)
            emit(it)
        }
    }

    /**
     * Finds a city by its ID or null if not.
     */
    override suspend fun findById(id: Long): City? =
        localDataSource.getById(id)?.copy(isFavorite = favoriteLocalStorage.findById(id))

    /**
     * Searches for cities whose names contain the query (case-insensitive), sorted by city then country.
     * If the local cache is empty, data is fetched from the API and cached before searching.
     *
     * @param query the search query string.
     * @return a [Flow] emitting a filtered and sorted list of cities.
     */
    override suspend fun searchCity(query: String): Flow<List<City>> = flow {
        localDataSource.get()?.let { fullList ->
            emit(searchAndSort(query, fullList))
        } ?: apiClient.getCityList().map {
            it.toDomain()
        }.also { fullList ->
            localDataSource.store(fullList)
            emit(searchAndSort(query, fullList))
        }
    }

    /**
     * Stores a list of cities locally.
     *
     * @param cityList the list of [City] to store.
     */
    override suspend fun store(cityList: List<City>) {
        localDataSource.store(cityList)
    }

    /**
     * Clear the local data sources.
     */
    override suspend fun clear() {
        localDataSource.clear()
        favoriteLocalStorage.clear()
    }

    /**
     * Adds a city to favorites.
     *
     * @param id the ID of the city.
     * @return true if the operation succeeded.
     */
    override suspend fun addFavorite(id: Long): Boolean = favoriteLocalStorage.addFavorite(id)

    /**
     * Removes a city to favorites.
     *
     * @param id the ID of the city.
     * @return true if the operation succeeded.
     */
    override suspend fun removeFavorite(id: Long): Boolean = favoriteLocalStorage.removeFavorite(id)

    /**
     * Filters and sorts a city list based on a search query.
     * Sorts alphabetically by city name, then by country code.
     *
     * @param query the search text.
     * @param fullList the full list of cities to search within.
     * @return the filtered and sorted list.
     */
    private suspend fun searchAndSort(query: String, fullList: List<City>): List<City> = fullList
        .filter { it.name.contains(query, ignoreCase = true) }
        .map { it.copy(isFavorite = favoriteLocalStorage.findById(it.id)) }
        .sortedWith(
            compareBy(
                { it.name.lowercase() },
                { it.countryCode.lowercase() }
            )
        )

    /**
     * Maps a [CityResponse] (from the API) to the domain [City] model.
     *
     * @return the mapped [City] object.
     */
    private fun CityResponse.toDomain() = City(
        id = id,
        name = name,
        countryCode = country,
        coordinate =
        coordinate.let {
            Coordinate(it.lon, it.lat)
        }
    )
}
