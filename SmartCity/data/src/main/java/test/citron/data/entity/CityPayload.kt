package test.citron.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class CityResponse(
    @SerialName("_id")
    val id: Long,
    val name: String,
    val country: String,
    @SerialName("coord")
    val coordinate: CoordinateResponse
)

@Keep
@Serializable
internal data class CoordinateResponse(
    val lon: Double,
    val lat: Double
)
