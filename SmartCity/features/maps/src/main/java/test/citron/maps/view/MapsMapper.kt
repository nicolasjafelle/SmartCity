package test.citron.maps.view

import test.citron.domain.model.City

class MapsMapper {

    fun toUiObject(city: City): UiObject = UiObject(
        id = city.id,
        label = "${city.name}, ${city.countryCode.uppercase()}",
        isFavorite = city.isFavorite,
        latitude = city.coordinate.latitude,
        longitude = city.coordinate.longitude
    )
}
