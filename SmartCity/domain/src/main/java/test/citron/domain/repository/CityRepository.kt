package test.citron.domain.repository

import kotlinx.coroutines.flow.Flow
import test.citron.domain.model.City

/**
 * Repository interface for managing city-related data.
 * Provides methods to fetch, store, search, and manipulate cities and their favorite status.
 */
interface CityRepository {

    suspend fun fetchCityList(): Boolean

    suspend fun store(cityList: List<City>)

    suspend fun getCityList(): Flow<List<City>>

    suspend fun searchCity(query: String): Flow<List<City>>

    suspend fun findById(id: Long): City?

    suspend fun addFavorite(id: Long): Boolean

    suspend fun removeFavorite(id: Long): Boolean

    suspend fun clear()
}
