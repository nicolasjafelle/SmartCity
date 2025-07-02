package test.citron.domain.usecase

import android.util.Log
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class FetchCityListUseCase(
    private val repository: CityRepository
) {
    suspend fun invoke(): Result<Boolean, CityError> = try {
        repository.fetchCityList().let {
            Result.Success(it)
        }
    } catch (ex: Exception) {
        Log.e("SearchCityUseCase", "error in ${ex.localizedMessage}", ex)
        CityErrorHandler.handleError(ex)
    }

//    fun invoke(): Flow<Result<List<City>, CityError>> =
//
//        repository.cachedCityList.filterNotNull().map<List<City>, Result<List<City>, CityError>> {
//            Result.Success(it)
//        }.catch { ex ->
//            Log.e("SearchCityUseCase", "error in ${ex.localizedMessage}", ex)
//            emit(CityErrorHandler.handleError(ex))
//        }
}
