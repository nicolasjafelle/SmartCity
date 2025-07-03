package test.citron.data.local

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import test.citron.domain.model.City

/**
 * In-memory implementation of [CityLocalStorage] which means simple use memory ram access.
 * If the process is killed, all data will be lost.
 *
 * Thread safety is ensured through the use of a [Mutex] to protect
 * concurrent access to the internal cache.
 */
internal class InMemoryCityStorage : CityLocalStorage {
    private var cache: MutableList<City>? = null

    // Mutex for thread-safe updates
    private val cacheMutex = Mutex()

    override suspend fun store(cityList: List<City>) {
        cacheMutex.withLock {
            cache?.clear()
            cache = mutableListOf<City>().also {
                it.addAll(cityList)
            }
        }
    }

    override suspend fun get(): List<City>? = cache

    override suspend fun getById(id: Long): City? = cache?.find { it.id == id }

    override suspend fun clear() {
        cacheMutex.withLock {
            cache?.clear()
        }
    }
}
