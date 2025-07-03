package test.citron.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import test.citron.domain.model.City
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class SearchCityUseCase(
    private val repository: CityRepository
) {

    /**
     * Executes a city search for the given [searchQuery].
     *
     * The result is a [Flow] that emits a [Result] containing either:
     * - a list of matching [City] instances on success,
     * - or a [CityError] on failure.
     *
     * @param searchQuery The query string to filter cities.
     * @return A [Flow] emitting [Result.Success] with city list or [Result.Error] with an error.
     */
    suspend fun invoke(searchQuery: String): Flow<Result<List<City>, CityError>> =
        repository.searchCity(searchQuery).map<List<City>, Result<List<City>, CityError>> {
            Result.Success(it)
        }.catch { ex ->
            Log.e("SearchCityUseCase", "error in ${ex.localizedMessage}", ex)
            emit(CityErrorHandler.handleError(ex))
        }
}
