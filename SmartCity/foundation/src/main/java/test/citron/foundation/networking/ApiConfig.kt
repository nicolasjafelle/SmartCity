package test.citron.foundation.networking

import test.citron.foundation.networking.ApiConfigurator.Companion.DEFAULT_TIMEOUT_IN_MILLIS

/**
 * Current API Configuration to be used across all the API requests/responses.
 */
class ApiConfig(private val theBaseUrl: String) :
    ApiConfigurator {
    override val baseUrl: String
        get() = theBaseUrl
    override val apiVersion: String
        get() = ApiConfigurator.Version.NONE.versionName
    override val timeout: Long
        get() = DEFAULT_TIMEOUT_IN_MILLIS
}
