package test.citron.foundation.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json

class KtorClient private constructor() {
    class Builder {
        private var baseUrl: String = ""
            set(value) {
                field = appendSlash(value)
            }

        private var apiVersion: String = DEFAULT_API_VERSION
            set(value) {
                field = appendSlash(value)
            }

        private var timeout: Long = ApiConfigurator.DEFAULT_TIMEOUT_IN_MILLIS

        fun setBaseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        fun setApiVersion(apiVersion: String) = apply { this.apiVersion = apiVersion }

        fun setTimeout(timeout: Long) = apply { this.timeout = timeout }

        fun build(): HttpClient {
            val finalBaseUrl = this@Builder.baseUrl + this@Builder.apiVersion

            val client =
                HttpClient(OkHttp) {
                    engine {
                        this.config {
                            connectTimeout(this@Builder.timeout, TimeUnit.MILLISECONDS)
                            readTimeout(this@Builder.timeout, TimeUnit.MILLISECONDS)
                            writeTimeout(this@Builder.timeout, TimeUnit.MILLISECONDS)
                        }
                    }

                    expectSuccess = true

                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                                encodeDefaults = false // avoid sending defaults (i.e.: nulls)
                            },
                            contentType = ContentType.Any
                        )
                    }

                    install(Logging) {
                        // LogLevel MUST NOT contain body information as Chucker will fail to collect
                        // it if the body is large enough (i.e. multipart files), throwing an exception
                        level = LogLevel.INFO
                        logger = Logger.Companion.SIMPLE
                    }

                    install(HttpTimeout) {
                        requestTimeoutMillis = this@Builder.timeout
                        connectTimeoutMillis = this@Builder.timeout
                        socketTimeoutMillis = this@Builder.timeout
                    }

                    install(DefaultRequest) {
                        url {
                            url(finalBaseUrl)
                            protocol = URLProtocol.HTTPS
                        }

                        header(HttpHeaders.ContentType, ContentType.Application.Json)
                    }
                }
            return client
        }

        private fun appendSlash(stringValue: String): String {
            return when {
                stringValue.endsWith("/") -> stringValue
                else -> "$stringValue/"
            }
        }

        companion object {
            private const val DEFAULT_API_VERSION = ""
        }
    }
}
