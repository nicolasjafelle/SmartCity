package test.citron.foundation.networking

interface ApiConfigurator {
    val baseUrl: String

    val apiVersion: String

    val timeout: Long

    // Future proofing
    enum class Version(val versionName: String) {
        NONE(""),
        VERSION_1("v1"),
        VERSION_2("v2"),
        VERSION_3("v3")
    }

    companion object {
        const val DEFAULT_TIMEOUT_IN_MILLIS: Long = 20 * 1000
    }
}
