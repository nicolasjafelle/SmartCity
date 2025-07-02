package test.citron.domain.model

import androidx.annotation.Keep

@Keep
data class City(
    val id: Long,
    val name: String,
    val isFavorite: Boolean = false,
    val countryCode: String,
    val coordinate: Coordinate
)
