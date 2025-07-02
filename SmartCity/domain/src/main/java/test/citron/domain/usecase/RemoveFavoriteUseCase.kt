package test.citron.domain.usecase

import android.util.Log
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class RemoveFavoriteUseCase(
    private val repository: CityRepository
) {
    suspend fun invoke(id: Long): Result<Boolean, CityError> = try {
        repository.removeFavorite(id).let { result ->
            if (result) {
                Result.Success(true)
            } else {
                Result.Error(CityError.Unknown)
            }
        }
    } catch (ex: Exception) {
        Log.e("RemoveFavoriteUseCase", "error in", ex)
        CityErrorHandler.handleError(ex)
    }
}
