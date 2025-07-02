package test.citron.domain.repository

import kotlinx.coroutines.flow.Flow
import test.citron.domain.model.City

interface CityRepository {

    val cachedCityList: Flow<List<City>?>

    suspend fun fetchCityList(): Boolean

    suspend fun store(citiList: List<City>)

    suspend fun getCityList(): Flow<List<City>>

    suspend fun searchCity(query: String): Flow<List<City>>

    suspend fun findById(id: Long): City?

    suspend fun addFavorite(id: Long): Boolean

    suspend fun removeFavorite(id: Long): Boolean

    suspend fun clear()
}
