package test.citron.domain.usecase

import android.util.Log
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class FetchCityListUseCase(
    private val repository: CityRepository
) {
    /**
     * Fetches the list of cities.
     *
     * If the list is already stored locally, it is reused. Otherwise, it attempts to fetch
     * from the API and store the result. Emits a [Result.Success] with `true` if cities were
     * fetched and stored successfully, or [Result.Error] with a [CityError] if something failed.
     *
     * @return [Result.Success] if the city list was loaded successfully, or
     *         [Result.Error] in case of failure.
     */
    suspend fun invoke(): Result<Boolean, CityError> = try {
        repository.fetchCityList().let {
            Result.Success(it)
        }
    } catch (ex: Exception) {
        Log.e("SearchCityUseCase", "error in ${ex.localizedMessage}", ex)
        CityErrorHandler.handleError(ex)
    }
}
