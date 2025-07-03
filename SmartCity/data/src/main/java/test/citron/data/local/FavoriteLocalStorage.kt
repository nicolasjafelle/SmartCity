package test.citron.data.local

/**
 * Interface which defines how to store and retrieve favorites in local storage.
 * Implementation of this interface usually are singleton and should provide proper
 * implementation where to store and retrieve data.
 * It could be in memory, in a database, protobuf or whatever.
 */
internal interface FavoriteLocalStorage {
    suspend fun store(favoriteListId: Set<Long>)

    suspend fun get(): Set<Long>?

    suspend fun findById(id: Long): Boolean

    suspend fun addFavorite(id: Long): Boolean

    suspend fun removeFavorite(id: Long): Boolean

    suspend fun clear()
}
