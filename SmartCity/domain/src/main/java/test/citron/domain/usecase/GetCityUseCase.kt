package test.citron.domain.usecase

import test.citron.domain.model.City
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class GetCityUseCase(
    private val repository: CityRepository
) {
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
