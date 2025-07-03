package test.citron.domain.usecase

import test.citron.domain.model.City
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class GetCityUseCase(
    private val repository: CityRepository
) {

    /**
     * Retrieves a city by its ID.
     * If the city is already stored locally, it is reused. Otherwise, it attempts to fetch
     * from the API and store the result. Emits a [Result.Success] with [City]
     * or [Result.Error] with a [CityError] if something failed.
     *
     * @return [Result.Success] the city fetched.
     *         [Result.Error] in case of failure.
     */
    suspend fun invoke(cityId: Long): Result<City, CityError> = try {
        repository.findById(cityId)?.let {
            Result.Success(it)
        } ?: run {
            Result.Error(CityError.CityNotFound)
        }
    } catch (ex: Exception) {
        CityErrorHandler.handleError(ex)
    }
}
