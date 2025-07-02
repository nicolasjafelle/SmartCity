package test.citron.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import test.citron.data.entity.CityResponse

internal interface CityApiClient {
    suspend fun getCityList(): List<CityResponse>

    class CityApiClientImpl(val client: HttpClient) : CityApiClient {
        override suspend fun getCityList(): List<CityResponse> {
            return client.get {
                header(HttpHeaders.Accept, "application/json")
                url(CITY_JSON)
            }.body<List<CityResponse>>()
        }
    }

    companion object {
        private const val CITY_JSON =
            "/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/" +
                "0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }
}
