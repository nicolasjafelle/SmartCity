package test.citron.data.local

import test.citron.domain.model.City

internal interface CityLocalStorage {
    suspend fun store(cityList: List<City>)

    suspend fun get(): List<City>?

    suspend fun getById(id: Long): City?

    suspend fun clear()
}
