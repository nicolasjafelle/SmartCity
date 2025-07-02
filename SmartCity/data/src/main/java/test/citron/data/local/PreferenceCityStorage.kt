package test.citron.data.local

import android.content.SharedPreferences
import androidx.core.content.edit

internal class PreferenceCityStorage(private val sharedPreferences: SharedPreferences) :
    FavoriteLocalStorage {
    override suspend fun store(favoriteListId: Set<Long>) {
        sharedPreferences.edit {
            putStringSet(FAVORITE_LIST_IDS, favoriteListId.map { it.toString() }.toSet())
        }
    }

    override suspend fun get(): Set<Long>? = sharedPreferences.getStringSet(
        FAVORITE_LIST_IDS,
        emptySet()
    )?.map {
        it.toLong()
    }?.toSet()

    override suspend fun findById(id: Long): Boolean = get()?.contains(id) ?: false

    override suspend fun addFavorite(id: Long): Boolean = get()?.let {
        store(it + id)
        true
    } ?: false

    override suspend fun removeFavorite(id: Long): Boolean = get()?.let {
        store(it - id)
        true
    } ?: false

    override suspend fun clear() {
        sharedPreferences.edit {
            remove(FAVORITE_LIST_IDS)
        }
    }

    companion object {
        private const val FAVORITE_LIST_IDS = "favoriteListIds"
    }
}
