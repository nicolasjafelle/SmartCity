package test.citron.data.local

import test.citron.domain.model.City

/**
 * Interface which defines how to store and retrieve data locally. Implementation of this interface
 * usually are singleton and should provide proper implementation where to store and retrieve data.
 * It could be in memory, in a database, protobuf or whatever.
 */
internal interface CityLocalStorage {
    suspend fun store(cityList: List<City>)

    suspend fun get(): List<City>?

    suspend fun getById(id: Long): City?

    suspend fun clear()
}
