package test.citron.domain.usecase

import android.util.Log
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class AddFavoriteUseCase(
    private val repository: CityRepository
) {
    /**
     * Adds the specified city to the list of favorites.
     *
     * @param id the unique identifier of the city to be added to favorites.
     * @return a [Result] containing:
     * - [Result.Success] with `true` if the city was successfully added.
     * - [Result.Error] with [CityError] if the operation failed or was not completed.
     */
    suspend fun invoke(id: Long): Result<Boolean, CityError> = try {
        repository.addFavorite(id).let { result ->
            if (result) {
                Result.Success(true)
            } else {
                Result.Error(CityError.Unknown)
            }
        }
    } catch (ex: Exception) {
        Log.e("AddFavoriteUseCase", "error in", ex)
        CityErrorHandler.handleError(ex)
    }
}
