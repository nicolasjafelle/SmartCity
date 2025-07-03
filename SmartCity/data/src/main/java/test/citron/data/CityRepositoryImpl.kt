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

internal class CityRepositoryImpl(
    private val apiClient: CityApiClient,
    private val localDataSource: CityLocalStorage,
    private val favoriteLocalStorage: FavoriteLocalStorage
) : CityRepository {

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

    override suspend fun getCityList(): Flow<List<City>> = flow {
        emit(localDataSource.get() ?: emptyList())

        apiClient.getCityList().map {
            it.toDomain()
        }.also {
            localDataSource.store(it)
            emit(it)
        }
    }

    override suspend fun findById(id: Long): City? =
        localDataSource.getById(id)?.copy(isFavorite = favoriteLocalStorage.findById(id))

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

    override suspend fun store(citiList: List<City>) {
        localDataSource.store(citiList)
    }

    override suspend fun clear() {
        localDataSource.clear()
        favoriteLocalStorage.clear()
    }

    override suspend fun addFavorite(id: Long): Boolean = favoriteLocalStorage.addFavorite(id)

    override suspend fun removeFavorite(id: Long): Boolean = favoriteLocalStorage.removeFavorite(id)

    private suspend fun searchAndSort(query: String, fullList: List<City>): List<City> = fullList
        .filter { it.name.contains(query, ignoreCase = true) }
        .map { it.copy(isFavorite = favoriteLocalStorage.findById(it.id)) }
        .sortedWith(
            compareBy(
                { it.name.lowercase() },
                { it.countryCode.lowercase() }
            )
        )

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
