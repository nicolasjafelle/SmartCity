package test.citron.search.view

import android.content.res.Resources
import test.citron.domain.model.City
import test.citron.domain.model.CityError
import test.citron.search.R

class SearchMapper(val resources: Resources) {
    fun mapList(cityList: List<City>): List<SearchResult> = cityList.map { toSearchResult(it) }

    fun toSearchResult(city: City): SearchResult = SearchResult(
        id = city.id,
        fullName = "${city.name}, ${city.countryCode}",
        isFavorite = city.isFavorite,
        latitude = city.coordinate.latitude,
        longitude = city.coordinate.longitude
    )

    fun resolveError(error: CityError): Error {
        val message =
            when (error) {
                CityError.Unknown -> resources.getString(R.string.search_error_unknown)
                CityError.NotFound -> resources.getString(R.string.search_error_not_found)
                CityError.ConnectionError -> resources.getString(R.string.search_error_connection)
                CityError.CityNotFound -> resources.getString(R.string.search_error_city_not_found)
            }
        return Error(message)
    }
}
