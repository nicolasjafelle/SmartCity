package test.citron.data.local

internal interface FavoriteLocalStorage {
    suspend fun store(favoriteListId: Set<Long>)

    suspend fun get(): Set<Long>?

    suspend fun findById(id: Long): Boolean

    suspend fun addFavorite(id: Long): Boolean

    suspend fun removeFavorite(id: Long): Boolean

    suspend fun clear()
}
