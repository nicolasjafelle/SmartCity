package test.citron.foundation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import test.citron.foundation.R
import test.citron.foundation.networking.ApiConfig
import test.citron.foundation.networking.ApiConfigurator
import test.citron.foundation.networking.KtorClient

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkHiltModule {
    @Provides
    fun providesApiConfigurator(@ApplicationContext context: Context): ApiConfigurator =
        ApiConfig(theBaseUrl = context.getString(R.string.BASE_URL))

    @Provides
    fun provideNetworkClient(apiConfig: ApiConfigurator): HttpClient {
        return KtorClient.Builder()
            .setBaseUrl(apiConfig.baseUrl)
            .setApiVersion(apiConfig.apiVersion)
            .setTimeout(apiConfig.timeout)
            .build()
    }
}
